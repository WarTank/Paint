package spengergasse.application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class ImageTestController {

    @FXML
    public ImageView imageView;

    public double getScaleXRatio()
    {
        return imageView.getBoundsInParent().getWidth()/imageView.getImage().getWidth();
    }

    public double getScaleYRatio()
    {
        return imageView.getBoundsInParent().getHeight()/imageView.getImage().getHeight();
    }

    @FXML
    public Label yPos;

    @FXML
    public Label xPos;

    @FXML
    public Button importButton;

    @FXML
    public Button saveButton;

    @FXML
    public SplitPane splitPane;

    @FXML
    public ColorPicker colorPicker;

    @FXML
    public TextField brushSizeTF;







    public double test(){
        return imageView.getFitWidth();
    }

}
