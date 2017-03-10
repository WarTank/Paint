package spengergasse.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spengergasse.model.MousePositionThread;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;

/**
 * Created by HER17491 on 10.03.2017.
 */
public class ImageTest extends Application {
    int mousePosX,mousePosY;
    ImageTestController itc;

    public ImageTestController getItc() {
        return itc;
    }

    public int getMousePosX() {
        return mousePosX;
    }

    public void setMousePosX(int mousePosX) {
        this.mousePosX = mousePosX;
    }

    public int getMousePosY() {
        return mousePosY;
    }

    public void setMousePosY(int mousePosY) {
        this.mousePosY = mousePosY;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Create Image and ImageView objects
            FXMLLoader loader = new FXMLLoader(ImageTestController.class.getResource("TestPanel.fxml"));
            Parent root = loader.load();
            itc = loader.getController();

            MousePositionThread mpt = new MousePositionThread(this);
            Thread mptt = new Thread(mpt);
            mptt.start();

            Scene scene = new Scene(root,1280,720);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            Image image = new Image("http://docs.oracle.com/javafx/"
                    + "javafx/images/javafx-documentation.png");
            itc.imageView.setImage(image);

            PixelReader pixelReader = image.getPixelReader();

            WritableImage wImage = new WritableImage(
                    (int)image.getWidth(),
                    (int)image.getHeight());
            PixelWriter pixelWriter = wImage.getPixelWriter();

            for(int readY=0;readY<image.getHeight();readY++){
                for(int readX=0; readX<image.getWidth();readX++){
                    Color color = pixelReader.getColor(readX,readY);

                    pixelWriter.setColor(readX,readY,color);
                }
            }

            itc.imageView.setImage(wImage);
            primaryStage.setTitle("Image Write Test");
            primaryStage.setScene(scene);
            primaryStage.show();
            changePixels();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changePixels(){

    }

    public static void main(String[] args) {
        launch(args);
    }
}
