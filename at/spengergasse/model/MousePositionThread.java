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

    public MousePositionThread(ImageTest imageTest) {
        this.imageTest = imageTest;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int x, y;
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            x = pointerInfo.getLocation().x;
            y = pointerInfo.getLocation().y;

            imageTest.setMousePosX(x);
            imageTest.setMousePosY(y);
            Platform.runLater(() -> {
                ImageTestController itc = imageTest.getItc();
                itc.xPos.setText("X:" + pointerInfo.getLocation().x);
                itc.yPos.setText("Y:" + pointerInfo.getLocation().y);
                //Very bad performance

                image = itc.imageView.getImage();

                int imageX, imageY;
                imageX = (int) image.getWidth();
                imageY = (int) image.getHeight();

                wImage = new WritableImage(imageX, imageY);

                pixelReader = image.getPixelReader();
                pixelWriter = wImage.getPixelWriter();


                for (int readY = 0; readY < image.getHeight(); readY++) {
                    for (int readX = 0; readX < image.getWidth(); readX++) {
                        Color color = pixelReader.getColor(readX, readY);

                        pixelWriter.setColor(readX, readY, color);
                    }
                }
                if ((x >= 0 && x < imageX) && (y >= 0 && y < imageY)) {
                    pixelWriter.setColor(x, y, Color.BLACK);

                }

                itc.imageView.setImage(wImage);
            });

            //System.out.println(pointerInfo.getLocation().x+"|"+pointerInfo.getLocation().y);
        }
    }
}
