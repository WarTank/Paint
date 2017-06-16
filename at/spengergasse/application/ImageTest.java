package spengergasse.application;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import spengergasse.model.MousePositionThread;
import spengergasse.model.Tool;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageTest extends Application {
    private int mousePosX;
    private int mousePosY;
    private boolean mousePressed;

    //TODO
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

    ImageTestController itc;

    //GETTERS AND SETTERS

    public ImageTestController getItc() {
        return itc;
    }

    public int getMousePosX() {
        return mousePosX;
    }

    public boolean isMousePressed() {
        return mousePressed;
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
            // BEGIN SETUP
            FXMLLoader loader = new FXMLLoader(ImageTestController.class.getResource("TestPanel.fxml"));
            Parent root = loader.load();
            itc = loader.getController();
            root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            MousePositionThread mpt = new MousePositionThread(this);
            Thread mptt = new Thread(mpt);
            mptt.start();


            //ensure only numbers are entered in textField
            itc.brushSizeTF.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    itc.brushSizeTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            itc.imageView.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                        mousePosX = (int) event.getX();
                        mousePosY = (int) event.getY();
                    } else if (event.isPrimaryButtonDown()) {
                        mousePosX = (int) event.getX();
                        mousePosY = (int) event.getY();
                        mousePressed = true;
                    } else {
                        mousePressed = false;
                    }
                }
            });

            //BUTTONS
            itc.saveButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extenstionFilterPng = new FileChooser.ExtensionFilter("Png File", "*.png");
                FileChooser.ExtensionFilter extenstionFilterJpg = new FileChooser.ExtensionFilter("Jpg File (Not working properly)", "*.jpg");
                fileChooser.getExtensionFilters().addAll(extenstionFilterPng, extenstionFilterJpg);


                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    BufferedImage bif = SwingFXUtils.fromFXImage(itc.imageView.getImage(), null);
                    String extension = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).substring(2);
                    try {
                        ImageIO.write(bif, extension, file);
                    } catch (IOException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });

            itc.saveButton.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

            itc.newButton.setOnAction(event -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("ResolutionPopup.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 280, 125);

                    ResolutionPopupController rpc = fxmlLoader.getController();

                    rpc.xResTF.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d*")) {
                                rpc.xResTF.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        }
                    });

                    rpc.xResTF.setOnAction(event1 -> {
                        rpc.yResTF.requestFocus();
                    });

                    rpc.yResTF.setOnAction(event1 -> {
                        rpc.doneButton.fire();
                    });

                    rpc.yResTF.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (!newValue.matches("\\d*")) {
                                rpc.yResTF.setText(newValue.replaceAll("[^\\d]", ""));
                            }
                        }
                    });


                    rpc.doneButton.setOnAction(event1 -> {
                        try {
                            WritableImage writableImage = new WritableImage(Integer.parseInt(rpc.xResTF.getText()), Integer.parseInt(rpc.yResTF.getText()));
                            System.out.println(rpc.transparentCheckBox == null);
                            if (!rpc.transparentCheckBox.isPressed()) {
                                PixelWriter writer = writableImage.getPixelWriter();
                                for (int x = 0; x < (int)writableImage.getWidth(); x++) {
                                    for (int y = 0; y < (int)writableImage.getHeight(); y++){
                                        writer.setColor(x,y, Color.WHITE);
                                    }
                                }
                            }
                            itc.imageView.setImage(writableImage);
                            scene.getWindow().hide();
                        }catch (Exception e){
                            rpc.errorTF.setText("Error");
                            rpc.xResTF.requestFocus();
                        }

                    });


                    Stage stage = new Stage();
                    stage.setResizable(false);
                    stage.setTitle("Set Resolution");
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            });

            itc.newButton.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

            itc.importButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
                fileChooser.getExtensionFilters().add(extensionFilter);
                File file = fileChooser.showOpenDialog(primaryStage);

                if (file != null) {
                    Image scaleImage = new Image(file.toURI().toString());
                    itc.imageView.setImage(scaleImage);
                }
            });

            itc.importButton.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

            itc.closeButton.setOnAction(event -> {
                if (primaryStage.isShowing()) {
                    primaryStage.close();
                }
            });

            itc.closeButton.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

            itc.clearButton.setOnAction(event -> {
                Image img = itc.imageView.getImage();
                itc.imageView.setImage(new WritableImage((int) img.getWidth(), (int) img.getHeight()));
            });

            //TOOL BUTTONS

            itc.brushButton.setOnAction(event -> {
                mpt.setCurrentTool(Tool.PaintBrush);
                itc.colorPicker.setDisable(false);
            });

            itc.eraserButton.setOnAction(event -> {
                mpt.setCurrentTool(Tool.Eraser);
                itc.colorPicker.setDisable(true);
            });

            itc.bucketButton.setOnAction(event -> {
                mpt.setCurrentTool(Tool.Bucket);
                itc.colorPicker.setDisable(false);
            });

            itc.colorPickerButton.setOnAction(event -> {
                mpt.setCurrentTool(Tool.EyeDropper);
                itc.colorPicker.setDisable(false);
            });

            //ZOOMING

            zoomProperty.addListener(arg0 -> {
                itc.imageView.setFitWidth(zoomProperty.get() * 4);
                itc.imageView.setFitHeight(zoomProperty.get() * 3);
            });

            itc.scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
                if (event.getDeltaY() > 0 && event.isControlDown()) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0 && event.isControlDown()) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            });

            //END SETUP

            Scene scene = new Scene(root, 1280, 720);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            Image image = new WritableImage(100, 100);
            itc.imageView.preserveRatioProperty().set(true);

            itc.imageView.setImage(image);
            primaryStage.setTitle("Paint Pro Plus");
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
