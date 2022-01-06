package common.src.UI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    Scene mainScene;
    Rectangle2D screenSize;
    Group group;

    @Override
    public void start(Stage primaryStage){
        screenSize = Screen.getPrimary().getBounds();
        double[] fullScreen = new double[] {screenSize.getHeight(), screenSize.getWidth()};
        //Lav 3 "objekter"
        //Læg dem i en gruppe
        mainScene = new Scene(group,(int) (0.7*fullScreen[0]), (int) (0.7*fullScreen[1]));
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
