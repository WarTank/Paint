package spengergasse.model.test;
/**
 * Created by wen17736 on 10.03.2017.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main extends Application {

    private Desktop desktop = Desktop.getDesktop();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();



        Button imp = new Button("Import");
        imp.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(final ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(primaryStage);
                if(file != null){
                    openFile(file);
                }
            }
        });





        Button secondButton = new Button("Save to");
        HBox hBox = new HBox(imp, secondButton);
        ToolBar toolbar = new ToolBar(hBox);
        borderPane.setTop(toolbar);


        Pane leftPane = new Pane();
        leftPane.getChildren().add(new ListView<Object>());
        SplitPane leftSplitPane = new SplitPane();
        leftSplitPane.getItems().add(leftPane);
        borderPane.setLeft(leftSplitPane);


        Canvas canvas = new Canvas(1, 1);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(canvas);
        borderPane.setCenter(stackPane);


        Pane rightPane = new Pane();
        rightPane.getChildren().add(new ListView<Object>());
        SplitPane rightSplitPane = new SplitPane();
        rightSplitPane.getItems().add(rightPane);
        borderPane.setRight(rightSplitPane);







        Scene scene = new Scene(borderPane, 300, 300);
        primaryStage.setTitle("Paint plus");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openFile(File file){
        try {
            desktop.open(file);
        }catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
