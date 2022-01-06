package TapasGames.UI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UIController extends Application {

    Scene mainScene;
    Rectangle2D screenSize;
    Group group = new Group();
    Label label = new Label("Big old titties");

    @Override
    public void start(Stage primaryStage){
        screenSize = Screen.getPrimary().getBounds();
        //Lav 3 "objekter"
        //Læg dem i en gruppe
        group.getChildren().add(label);
        mainScene = new Scene(group,(int) (0.7*screenSize.getHeight()), (int) (0.7*screenSize.getWidth()));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
