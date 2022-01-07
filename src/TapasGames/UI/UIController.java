package TapasGames.UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UIController extends Application {

    Scene mainScene;
    Rectangle2D screenSize;
    GridPane buttons = new GridPane();
    GridPane grid = new GridPane();
    Button exit = new Button("Exit");
    Button settings = new Button("Settings");
    Button vts = new Button("Vote to skip");
    Separator separatorH = new Separator(Orientation.HORIZONTAL);
    Separator separatorV1 = new Separator(Orientation.VERTICAL);
    Separator separatorV2 = new Separator(Orientation.VERTICAL);
    Separator separatorV3 = new Separator(Orientation.VERTICAL);
    VBox team1 = new VBox();
    Label team1L = new Label("Team 1");
    Label team1S = new Label("0");
    VBox team2 = new VBox();
    Label team2L = new Label("Team 2");
    Label team2S = new Label("0");
    VBox team3 = new VBox();
    Label team3L = new Label("Team 3");
    Label team3S = new Label("0");
    VBox team4 = new VBox();
    Label team4L = new Label("Team 4");
    Label team4S = new Label("0");
    HBox teams = new HBox();
    Label points = new Label("Points");
    VBox score = new VBox();

    //TEMP
    Button controls = new Button("Controls");
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

        team1.setVgrow(team1L,Priority.NEVER);
        team1.setVgrow(team1S,Priority.ALWAYS);
        team1L.setMaxWidth(Double.MAX_VALUE);
        team1S.setMaxWidth(Double.MAX_VALUE);
        team1S.setMaxHeight(Double.MAX_VALUE);
        team1L.setAlignment(Pos.TOP_CENTER);
        team1S.setAlignment(Pos.CENTER);
        team1.getChildren().addAll(team1L,team1S);

        team2.setVgrow(team2L,Priority.NEVER);
        team2.setVgrow(team2S,Priority.ALWAYS);
        team2L.setMaxWidth(Double.MAX_VALUE);
        team2S.setMaxWidth(Double.MAX_VALUE);
        team2S.setMaxHeight(Double.MAX_VALUE);
        team2L.setAlignment(Pos.TOP_CENTER);
        team2S.setAlignment(Pos.CENTER);
        team2.getChildren().addAll(team2L,team2S);

        team3.setVgrow(team3L,Priority.NEVER);
        team3.setVgrow(team3S,Priority.ALWAYS);
        team3L.setMaxWidth(Double.MAX_VALUE);
        team3S.setMaxWidth(Double.MAX_VALUE);
        team3S.setMaxHeight(Double.MAX_VALUE);
        team3L.setAlignment(Pos.TOP_CENTER);
        team3S.setAlignment(Pos.CENTER);
        team3.getChildren().addAll(team3L,team3S);

        team4.setVgrow(team4L,Priority.NEVER);
        team4.setVgrow(team4S,Priority.ALWAYS);
        team4L.setMaxWidth(Double.MAX_VALUE);
        team4S.setMaxWidth(Double.MAX_VALUE);
        team4S.setMaxHeight(Double.MAX_VALUE);
        team4L.setAlignment(Pos.TOP_CENTER);
        team4S.setAlignment(Pos.CENTER);
        team4.getChildren().addAll(team4L,team4S);

        teams.setHgrow(team1,Priority.ALWAYS);
        teams.setHgrow(team2,Priority.ALWAYS);
        teams.setHgrow(team3,Priority.ALWAYS);
        teams.setHgrow(team4,Priority.ALWAYS);
        team1.setMaxHeight(Double.MAX_VALUE);
        team2.setMaxHeight(Double.MAX_VALUE);
        team3.setMaxHeight(Double.MAX_VALUE);
        team4.setMaxHeight(Double.MAX_VALUE);
        teams.getChildren().addAll(team1,separatorV1,team2);
        //teams.getChildren().addAll(separatorV2,team3); //Adds team 3
        //teams.getChildren().addAll(separatorV3,team4); //Adds team 4
        //teams.getChildren().removeAll(separatorV3,team4); //Removes team 4

        score.setVgrow(teams,Priority.ALWAYS);
        score.setVgrow(points,Priority.NEVER);
        teams.setMaxWidth(Double.MAX_VALUE);
        points.setMaxWidth(Double.MAX_VALUE);
        points.setAlignment(Pos.TOP_CENTER);
        score.getChildren().addAll(points,separatorH,teams);
        score.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);

        game.setPrefSize(windowSize[0]*0.75,windowSize[1]*0.8);
        buttons.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);
        controls.setPrefSize(windowSize[0]*0.25,windowSize[1]*0.2);
        chat.setPrefSize(windowSize[0]*0.25,windowSize[1]);

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

        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final Stage settingsDialog = new Stage();
                settingsDialog.initModality(Modality.APPLICATION_MODAL);
                settingsDialog.initOwner(primaryStage);
                VBox content = new VBox();
                content.getChildren().add(new Label("Ulrik dum"));
                Scene settingsScene = new Scene(content,windowSize[0]/4,windowSize[1]/2);
                settingsDialog.setScene(settingsScene);
                settingsDialog.setResizable(false);
                settingsDialog.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue)
                        settingsDialog.setMaximized(false);
                });
                settingsDialog.show();
            }
        });

        mainScene = new Scene(grid, (int) windowSize[0],(int) windowSize[1]);
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
