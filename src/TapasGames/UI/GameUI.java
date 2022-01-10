package TapasGames.UI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;


public class GameUI extends Application {

    Rectangle2D configureScreenSize;
    double size = 0.7;
    Stage gameStage;
    Stage menuStage;
    Stage chatStage;

    @Override
    public void start(Stage stage) throws Exception{
        configureScreenSize = Screen.getPrimary().getBounds();

        GameScene();
        MenuScene();
        ChatScene();

    }

    public void GameScene(){
        gameStage = new Stage();
        gameStage.setTitle("Game Window");

        gameStage.setWidth(configureScreenSize.getWidth() * size * 0.75);
        gameStage.setHeight(configureScreenSize.getHeight() * size * 0.75);

        gameStage.setX(configureScreenSize.getWidth() * 0.15);
        gameStage.setY(configureScreenSize.getHeight() * 0.15);

        gameStage.setResizable(false);

        gameStage.show();

    }

    public void MenuScene(){
        menuStage = new Stage();
        menuStage.setTitle("Menu Window");
        menuStage.setWidth(configureScreenSize.getWidth()*size*0.75);
        menuStage.setHeight(configureScreenSize.getHeight()*size*0.25);

        menuStage.setX(configureScreenSize.getWidth() * 0.15);
        menuStage.setY(configureScreenSize.getHeight() * 0.15 + configureScreenSize.getHeight() * size * 0.75);

        menuStage.initOwner(gameStage);
        menuStage.setResizable(false);
        menuStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
        });

        GridPane buttons = new GridPane();
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
        Button controls = new Button("Controls");

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
        score.setPrefSize(configureScreenSize.getWidth()*size*0.25,configureScreenSize.getHeight()*size*0.2);
        buttons.setPrefSize(configureScreenSize.getWidth()*size*0.25,configureScreenSize.getHeight()*size*0.2);
        controls.setPrefSize(configureScreenSize.getWidth()*size*0.25,configureScreenSize.getHeight()*size*0.2);

        settings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final Stage settingsDialog = new Stage();
                settingsDialog.initModality(Modality.APPLICATION_MODAL);
                settingsDialog.initOwner(menuStage);
                VBox content = new VBox();
                content.getChildren().add(new Label("Ulrik dum"));
                Scene settingsScene = new Scene(content,configureScreenSize.getWidth()/4,configureScreenSize.getHeight()/2);
                settingsDialog.setScene(settingsScene);
                settingsDialog.setResizable(false);
                settingsDialog.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue)
                        settingsDialog.setMaximized(false);
                });
                settingsDialog.show();
            }
        });

        HBox combi = new HBox();
        combi.getChildren().addAll(buttons,controls,score);
        Scene menuScene = new Scene(combi,configureScreenSize.getWidth() * size * 0.75,configureScreenSize.getHeight() * size * 0.25);

        menuStage.setScene(menuScene);
        menuStage.show();
    }

    public void ChatScene(){
        chatStage = new Stage();
        chatStage.setTitle("Chat Window");

        chatStage.setWidth(configureScreenSize.getWidth() * 0.7 * 0.25);
        chatStage.setHeight(configureScreenSize.getHeight() * 0.70);

        chatStage.setX(configureScreenSize.getWidth() * 0.15 + configureScreenSize.getWidth() * 0.7 * 0.75);
        chatStage.setY(configureScreenSize.getHeight() * 0.15);

        chatStage.setResizable(false);
        chatStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
        });
        chatStage.initOwner(gameStage);

        //Pane used for the layout
        BorderPane layout = new BorderPane();

        //<editor-fold desc="Code for: Global/Team Chat + header">
        VBox chatHeader = new VBox();
        chatHeader.setPadding(new Insets(10, 10, 10, 10));

        Label headerText = new Label("Messages");
        headerText.setFont(new Font("Arial", 30));
        chatHeader.setAlignment(Pos.TOP_CENTER);

        Button globalChat = new Button("global");
        Button teamChat = new Button("Team");

        HBox chatButtons = new HBox(globalChat, teamChat);
        chatButtons.setAlignment(Pos.TOP_CENTER);

        chatHeader.getChildren().addAll(headerText, chatButtons);

        layout.setTop(chatHeader);
        //</editor-fold>

        //<editor-fold desc="Code for: Messages sent to the chat">
        ScrollPane chatPane = new ScrollPane();
        chatPane.setPadding(new Insets(10, 10, 10, 10));



        layout.setCenter(chatPane);
        //</editor-fold>

        //<editor-fold desc="Code for: Sent Message Text-field/Button">
        //Make message send box
        VBox sendMessageBar = new VBox();
        sendMessageBar.setPadding(new Insets(10, 10, 10, 10));

        TextField messageBox = new TextField();
        messageBox.setPromptText("Write Message");
        messageBox.setPrefColumnCount(10);
        messageBox.getText();

        //Make send button
        Button sendButtons = new Button("Send");

        sendMessageBar.getChildren().addAll(messageBox, sendButtons);

        layout.setBottom(sendMessageBar);

        //</editor-fold>

        Scene chatScene = new Scene(layout);
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
