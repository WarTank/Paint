package spengergasse.model;

import javafx.application.Platform;
import spengergasse.application.ImageTest;
import spengergasse.application.ImageTestController;

import java.awt.*;

/**
 * Created by HER17491 on 10.03.2017.
 */
public class MousePositionThread implements Runnable{

    private ImageTest imageTest;

    public MousePositionThread(ImageTest imageTest) {
        this.imageTest = imageTest;
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            imageTest.setMousePosX(pointerInfo.getLocation().x);
            imageTest.setMousePosY(pointerInfo.getLocation().y);
            Platform.runLater(() -> {
                imageTest.getItc().xPos.setText("X:"+pointerInfo.getLocation().x);
                imageTest.getItc().yPos.setText("Y:"+pointerInfo.getLocation().y);
            });

            //System.out.println(pointerInfo.getLocation().x+"|"+pointerInfo.getLocation().y);
        }
    }
}
