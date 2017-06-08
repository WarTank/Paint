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

            //Main Thread Code
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

                int halfBrushSize = brushSize / 2;

                if (isWithinNumbers(scaledX, 0, imageX) && isWithinNumbers(scaledY, 0, imageY)) {
                    if (brushSize == 1) {
                        placeColorFromPicker(scaledX, scaledY, pixelWriter);
                    } else {
                        //Loop through pixel square with cursor position as center
                        for (int i = -halfBrushSize; i < brushSize - halfBrushSize; i++) {
                            for (int j = -halfBrushSize; j < brushSize - halfBrushSize; j++) {

                                int currentlyPlacingX, currentlyPlacingY;
                                currentlyPlacingX = scaledX + i;
                                currentlyPlacingY = scaledY + j;

                                //Convert current position to center to vector
                                Vec2d vec2d = convertPositionsToVector(scaledX, scaledY, currentlyPlacingX, currentlyPlacingY);

                                //Checks if the current position is within the brushSizeRadius
                                if (isWithinRadius((int) vec2d.x, (int) vec2d.y, halfBrushSize)) {
                                    //Checks whether this particular pixel would be out of bounds
                                    if ((isWithinNumbers(currentlyPlacingX, 0, imageX) && isWithinNumbers(currentlyPlacingY, 0, imageY))) {
                                        placeColorFromPicker(currentlyPlacingX, currentlyPlacingY, pixelWriter);
                                    }
                                }
                            }
                        }
                    }

                }

                itc.imageView.setImage(wImage);
            });
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
