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
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIController extends Application {

    Scene mainScene;
    Rectangle2D screenSize;
    Label label = new Label("Big old titties");
    GridPane buttons = new GridPane();
    GridPane grid = new GridPane();
    Button exit = new Button("Exit");
    Button settings = new Button("Settings");
    Button vts = new Button("Vote to skip");
    Separator separatorH = new Separator(Orientation.HORIZONTAL);
    Separator separatorV = new Separator(Orientation.VERTICAL);

    //TEMP
    Button controls = new Button("Controls");
    Button score = new Button("Score");
    Button game = new Button("Game");
    Button chat = new Button("Chat");

    @Override
    public void start(Stage primaryStage){
        screenSize = Screen.getPrimary().getBounds();
        double[] windowSize = {0.7*screenSize.getWidth(), 0.7*screenSize.getHeight()};
        exit.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        settings.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        vts.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        buttons.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);
        buttons.add(exit,0,0,1,2);
        buttons.add(settings,1,0,1,1);
        buttons.add(vts,1,1,1,1);
        buttons.setVgrow(exit, Priority.ALWAYS);
        buttons.setVgrow(settings,Priority.ALWAYS);
        buttons.setVgrow(vts,Priority.ALWAYS);
        buttons.setHgrow(exit,Priority.ALWAYS);
        buttons.setHgrow(settings,Priority.ALWAYS);
        buttons.setHgrow(vts,Priority.ALWAYS);
        buttons.setVgap(5);
        buttons.setHgap(5);

        //TEMP
        game.setPrefSize(windowSize[0]*0.75,windowSize[1]*0.8);
        buttons.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);
        controls.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);
        score.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);
        chat.setPrefSize(windowSize[0]*0.25,windowSize[1]);

        //grid.getStyleClass().add("-fx - background - fill: black, white; fx - background - insets: 0, 1;");
        grid.add(game,0,0,15,16);
        grid.add(buttons,0,16,5,4);
        grid.add(controls,5,16,5,4);
        grid.add(score,10,16,5,4);
        grid.add(chat,15,0,5,20);

        grid.setHgrow(game,Priority.ALWAYS);
        grid.setVgrow(game,Priority.ALWAYS);
        grid.setHgrow(buttons,Priority.ALWAYS);
        grid.setVgrow(buttons,Priority.ALWAYS);
        grid.setHgrow(controls,Priority.ALWAYS);
        grid.setVgrow(controls,Priority.ALWAYS);
        grid.setHgrow(score,Priority.ALWAYS);
        grid.setVgrow(score,Priority.ALWAYS);
        grid.setHgrow(chat,Priority.ALWAYS);
        grid.setVgrow(chat,Priority.ALWAYS);

        mainScene = new Scene(grid, (int) windowSize[0],(int) windowSize[1]);
        label.setText("" + mainScene.getWidth() + ", " + mainScene.getHeight());
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                primaryStage.setMaximized(false);
        });
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
