package TapasGames.UI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @Override
    public void start(Stage primaryStage){
        screenSize = Screen.getPrimary().getBounds();
        buttons.add(exit,0,0,1,2);
        buttons.add(settings,0,1,1,1);
        buttons.add(vts,1,1,1,1);
        //Lav 3 "objekter"
        //Læg dem i en gruppe
        group.getChildren().add(label);
        mainScene = new Scene(group, (int) (0.7*screenSize.getWidth()),(int) (0.7*screenSize.getHeight()));
        label.setText("" + mainScene.getWidth() + ", " + mainScene.getHeight());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
