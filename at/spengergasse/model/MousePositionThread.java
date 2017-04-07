package spengergasse.model;

import javafx.application.Platform;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import spengergasse.application.ImageTest;
import spengergasse.application.ImageTestController;

import java.awt.*;

public class MousePositionThread implements Runnable {

    private ImageTest imageTest;
    private WritableImage wImage;
    private PixelReader pixelReader;
    private PixelWriter pixelWriter;
    private Image image;
    private PointerInfo pointerInfo;
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
            brushSize = itc.brushSizeTF.getText().isEmpty() ? 3 : Integer.parseInt(itc.brushSizeTF.getText());

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            x = imageTest.getMousePosX();
            y = imageTest.getMousePosY();

            /*pointerInfo = MouseInfo.getPointerInfo();
            x = pointerInfo.getLocation().x;
            y = pointerInfo.getLocation().y;

            imageTest.setMousePosX(x);
            imageTest.setMousePosY(y);*/

            //Main Thread Code
            Platform.runLater(() -> {
                itc.xPos.setText("X:" + x);
                itc.yPos.setText("Y:" + y);
                //Very bad performance

                image = itc.imageView.getImage();

                int imageX, imageY;
                imageX = (int) image.getWidth();
                imageY = (int) image.getHeight();

                pixelReader = image.getPixelReader();

                wImage = new WritableImage(pixelReader, imageX, imageY);

                pixelWriter = wImage.getPixelWriter();

                scaledX = (int) (x / itc.getScaleXRatio());
                scaledY = (int) (y / itc.getScaleYRatio());

                if ((scaledX >= 0 && x < imageX) && (scaledY >= 0 && y < imageY)) {
                    for (int i = 0; i < brushSize; i++) {
                        for (int j = 0; j < brushSize; j++) {

                        }
                    }
                    pixelWriter.setColor(scaledX, scaledY, itc.colorPicker.getValue());
                }

                itc.imageView.setImage(wImage);
            });

            //System.out.println(pointerInfo.getLocation().x+"|"+pointerInfo.getLocation().y);
        }
    }

    public void UpdateMouseCoordinates() {

    }
}
