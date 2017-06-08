package spengergasse.model;

import com.sun.javafx.geom.Vec2d;
import javafx.application.Platform;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import spengergasse.application.ImageTest;
import spengergasse.application.ImageTestController;

public class MousePositionThread implements Runnable {

    private ImageTest imageTest;
    private WritableImage wImage;
    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private Image image;
    private ImageTestController itc;

    private int x, y;

    private int scaledX, scaledY;

    public MousePositionThread(ImageTest imageTest) {
        this.imageTest = imageTest;
    }

    private int brushSize;

    private Vec2d prevCoords;

    private boolean mousePressedLastFrame;

    @Override
    public void run() {
        itc = imageTest.getItc();

        while (true) {
            brushSize = itc.brushSizeTF.getText().isEmpty() ? 1 : Integer.parseInt(itc.brushSizeTF.getText());

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x = imageTest.getMousePosX();
            y = imageTest.getMousePosY();

            //ResolutionPopup Thread Code
            Platform.runLater(() -> {
                itc.xPos.setText("X:" + x);
                itc.yPos.setText("Y:" + y);

                image = itc.imageView.getImage();

                int imageX, imageY;
                imageX = (int) image.getWidth();
                imageY = (int) image.getHeight();

                pixelReader = image.getPixelReader();

                wImage = new WritableImage(pixelReader, imageX, imageY);

                pixelWriter = wImage.getPixelWriter();

                //scales the display x coordinates to the imageView scale ratio
                scaledX = (int) (x / itc.getScaleXRatio());
                scaledY = (int) (y / itc.getScaleYRatio());

                Vec2d currPos = new Vec2d(scaledX, scaledY);

                if (isWithinNumbers(scaledX, 0, imageX) && isWithinNumbers(scaledY, 0, imageY)) {

                    if (prevCoords != null) {
                        if (mousePressedLastFrame) {
                            int amountOfPoints = (int) (getLengthOfVector(getVectorBetweenTwoPos(prevCoords, currPos)) / ((float) brushSize / 8));
                            for (int i = 0; i < amountOfPoints; i++) {
                                Vec2d point = getPointBetweenVectors(prevCoords, currPos, 1f / amountOfPoints * i);
                                placeCircleAtCoordinates((int) point.x, (int) point.y, brushSize, imageX, imageY);
                            }
                        } else {
                            placeCircleAtCoordinates(scaledX, scaledY, brushSize, imageX, imageY);
                        }
                    }
                }


                itc.imageView.setImage(wImage);
                mousePressedLastFrame = imageTest.isMousePressed();

                prevCoords = new Vec2d(scaledX, scaledY);
            });
        }
    }

    private Vec2d getPointBetweenVectors(Vec2d A, Vec2d B, float between) {
        Vec2d AB = getVectorBetweenTwoPos(A, B);

        return new Vec2d(A.x + AB.x * between, A.y + AB.y * between);
    }

    private Vec2d getVectorBetweenTwoPos(Vec2d A, Vec2d B) {
        return new Vec2d(B.x - A.x, B.y - A.y);
    }

    private float getLengthOfVector(Vec2d vec) {
        return (float) Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2));
    }

    private void placeCircleAtCoordinates(int x, int y, int size, int imageWidth, int imageHeight) {
        int halfBrushSize = size / 2;

        if (brushSize == 1) {
            int currentlyPlacingX, currentlyPlacingY;
            currentlyPlacingX = x;
            currentlyPlacingY = y;

            if ((isWithinNumbers(currentlyPlacingX, 0, imageWidth) && isWithinNumbers(currentlyPlacingY, 0, imageHeight))) {
                placeColorFromPicker(currentlyPlacingX, currentlyPlacingY, pixelWriter);
            }

            return;
        }


        for (int i = -halfBrushSize; i < size - halfBrushSize; i++) {
            for (int j = -halfBrushSize; j < size - halfBrushSize; j++) {

                int currentlyPlacingX, currentlyPlacingY;
                currentlyPlacingX = x + i;
                currentlyPlacingY = y + j;

                //Convert current position to center to vector
                Vec2d vec2d = convertPositionsToVector(x, y, currentlyPlacingX, currentlyPlacingY);

                //Checks if the current position is within the brushSizeRadius
                if (isWithinRadius((int) vec2d.x, (int) vec2d.y, halfBrushSize)) {
                    //Checks whether this particular pixel would be out of bounds
                    if ((isWithinNumbers(currentlyPlacingX, 0, imageWidth) && isWithinNumbers(currentlyPlacingY, 0, imageHeight))) {
                        placeColorFromPicker(currentlyPlacingX, currentlyPlacingY, pixelWriter);
                    }
                }
            }
        }
    }

    private void placeColorFromPicker(int x, int y, PixelWriter pixelWriter) {
        pixelWriter.setColor(x, y, itc.colorPicker.getValue());
    }

    private Vec2d convertPositionsToVector(int x1, int y1, int x2, int y2) {
        return new Vec2d((x2 - x1), (y2 - y1));
    }

    private boolean isWithinRadius(int x, int y, int radius) {
        return Math.abs(Math.sqrt(x * x + y * y)) < radius;
    }

    private boolean isWithinNumbers(int n, int min, int max) {
        return (n > min && n < max);
    }

}
