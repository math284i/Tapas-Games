package TapasGames.UI;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UIController extends Application {

    Scene mainScene;
    Rectangle2D screenSize;
    Group group = new Group();
    Label label = new Label("Big old titties");
    HBox info;
    GridPane buttons = new GridPane();
    Button exit = new Button("Exit");
    Button settings = new Button("Settings");
    Button vts = new Button("Vote to skip");
    Separator separatorH = new Separator(Orientation.HORIZONTAL);
    Separator separatorV = new Separator(Orientation.VERTICAL);

    @Override
    public void start(Stage primaryStage){
        screenSize = Screen.getPrimary().getBounds();
        double[] windowSize = {0.7*screenSize.getWidth(), 0.7*screenSize.getHeight()};
        exit.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        settings.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        vts.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        buttons.add(exit,0,0,2,1);
        buttons.add(settings,0,1,1,1);
        buttons.add(vts,1,1,1,1);
        buttons.setVgap(5);
        buttons.setHgap(5);


        info = new HBox(buttons,separatorV,label);
        info.setPrefWidth(0.75*windowSize[0]);
        info.setPrefHeight(0.2*windowSize[1]);
        //Lav 3 "objekter"
        //Læg dem i en gruppe
        group.getChildren().addAll(info);
        mainScene = new Scene(group, (int) windowSize[0],(int) windowSize[1]);
        label.setText("" + mainScene.getWidth() + ", " + mainScene.getHeight());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
