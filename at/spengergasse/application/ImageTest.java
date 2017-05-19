package spengergasse.application;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import spengergasse.model.MousePositionThread;

import javax.imageio.ImageIO;
import java.io.*;


public class ImageTest extends Application {
    private int mousePosX;
    private int mousePosY;

    //TODO
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

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


            //ensure only numbers are entered in textField
            itc.brushSizeTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    itc.brushSizeTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            itc.splitPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getEventType() == MouseEvent.MOUSE_MOVED){
                        double splitDeviderWidth= itc.splitPane.getWidth() * itc.splitPane.getDividerPositions()[0];
                        mousePosX = (int)(event.getSceneX() - splitDeviderWidth);
                        mousePosY = (int)event.getSceneY();
                    }


                }
            });

            itc.saveButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
                fileChooser.getExtensionFilters().add(extensionFilter);

                File file = fileChooser.showSaveDialog(primaryStage);

                PixelReader pixelReader = itc.imageView.getImage().getPixelReader();
                WritableImage wim = new WritableImage(pixelReader, (int) itc.imageView.getBoundsInLocal().getWidth(), (int) itc.imageView.getBoundsInLocal().getHeight());
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "jpg", file);
                }catch (IOException e){
                    e.printStackTrace();
                }





            });

            itc.importButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
                fileChooser.getExtensionFilters().add(extensionFilter);
                File file = fileChooser.showOpenDialog(primaryStage);
                if(file != null){
                    itc.imageView.setImage(new Image(file.toURI().toString()));
                }
            });

            zoomProperty.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable arg0) {
                    itc.imageView.setFitWidth(zoomProperty.get() * 4);
                    itc.imageView.setFitHeight(zoomProperty.get() * 3);
                }
            });

            itc.splitPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
                public void handle(ScrollEvent event){
                    if (event.getDeltaY() > 0 && event.isControlDown()) {
                        zoomProperty.set(zoomProperty.get() * 1.1);
                    } else if (event.getDeltaY() < 0 && event.isControlDown()) {
                        zoomProperty.set(zoomProperty.get() / 1.1);
                    }
                }
            });

            Scene scene = new Scene(root,1280,720);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            Image image = new Image("https://static-whitecastle-com.s3.amazonaws.com/spacer.gif");
            itc.imageView.preserveRatioProperty().set(true);

            itc.imageView.setImage(image);
            primaryStage.setTitle("Image Write Test");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
