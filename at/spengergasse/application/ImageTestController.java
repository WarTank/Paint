package spengergasse.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class ImageTestController {

    @FXML
    public ImageView imageView;

    public double getScaleXRatio() {
        return imageView.getBoundsInParent().getWidth() / imageView.getImage().getWidth();
    }

    public double getScaleYRatio() {
        return imageView.getBoundsInParent().getHeight() / imageView.getImage().getHeight();
    }

    @FXML
    public Label yPos;

    @FXML
    public Label xPos;

    @FXML
    public MenuItem newButton;

    @FXML
    public MenuItem importButton;

    @FXML
    public MenuItem saveButton;

    @FXML
    public MenuItem clearButton;


    @FXML
    public ColorPicker colorPicker;

    @FXML
    public TextField brushSizeTF;

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public MenuItem closeButton;

    @FXML
    public Button brushButton;

    @FXML
    public Button eraserButton;

    @FXML
    public Button bucketButton;

    @FXML
    public FlowPane flowPane;

    @FXML
    public Button colorPickerButton;


    public double test() {
        return imageView.getFitWidth();
    }

}
