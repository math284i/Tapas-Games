package TapasGames.UiFiles;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LobbyUI extends Application {
    Rectangle2D configureScreenSize;

    double size = 0.7;

    Stage LobbyStage;

    @Override
    public void start(Stage stage) throws Exception {
        configureScreenSize = Screen.getPrimary().getBounds();

        LobbyScene();
    }

    public  void LobbyScene() throws Exception{
        LobbyStage = new Stage();
        LobbyStage.setTitle("Lobby");

        LobbyStage.setWidth(configureScreenSize.getHeight() * size);
        LobbyStage.setHeight(configureScreenSize.getHeight() * size);

        LobbyStage.setResizable(false);

        VBox Layout = new VBox();
        Layout.setAlignment(Pos.CENTER);

        //Lobby header
        Label header = new Label("Lobby");
        header.setFont(new Font(60));


        //Participants


        //Code for Player 1
        Label p1Name = new Label("Player: " + "Name1"); //<-- Change to players name

        Label p1Status = new Label("Status: ");
        Label display1Status = new Label("Not Ready"); //<-- Change to players status
        HBox status1 = new HBox();
        status1.getChildren().addAll(p1Status, display1Status);

        p1Name.setFont(new Font(30));
        p1Status.setFont(new Font(30));
        display1Status.setFont(new Font(30));

        VBox player1 = new VBox();
        player1.getChildren().addAll(p1Name,status1);


        //Code for Player 2
        Label p2Name = new Label("Player: " + "Name2"); //<-- Change to players name

        Label p2Status = new Label("Status: ");
        Label display2Status = new Label("Not Ready"); //<-- Change to players status
        HBox status2 = new HBox();
        status2.getChildren().addAll(p2Status, display2Status);

        p2Name.setFont(new Font(30));
        p2Status.setFont(new Font(30));
        display2Status.setFont(new Font(30));

        VBox player2 = new VBox();
        player2.getChildren().addAll(p2Name,status2);


        //Code for Player 3
        Label p3Name = new Label("Player: " + "Name3"); //<-- Change to players name

        Label p3Status = new Label("Status: ");
        Label display3Status = new Label("Not Ready"); //<-- Change to players status
        HBox status3 = new HBox();
        status3.getChildren().addAll(p3Status, display3Status);

        p3Name.setFont(new Font(30));
        p3Status.setFont(new Font(30));
        display3Status.setFont(new Font(30));

        VBox player3 = new VBox();
        player3.getChildren().addAll(p3Name,status3);


        //Code for Player 4
        Label p4Name = new Label("Player: " + "Name4"); //<-- Change to players name

        Label p4Status = new Label("Status: ");
        Label display4Status = new Label("Not Ready"); //<-- Change to players status
        HBox status4 = new HBox();
        status4.getChildren().addAll(p4Status, display4Status);

        p4Name.setFont(new Font(30));
        p4Status.setFont(new Font(30));
        display4Status.setFont(new Font(30));

        VBox player4 = new VBox();
        player4.getChildren().addAll(p4Name,status4);


        //Set Pos of players
        GridPane participants = new GridPane();

        participants.setPadding(new Insets(10, 10, 10, 10));
        participants.setMinSize(300, 300);
        participants.setVgap(5);
        participants.setHgap(5);

        participants.add(player1, 0, 0, 1, 1);
        participants.add(player2, 6, 0, 1, 1);
        participants.add(player3, 0, 6, 1, 1);
        participants.add(player4, 6 ,6, 1, 1);

        participants.setAlignment(Pos.CENTER);


        //Ready Button
        Button readyButton = new Button("Ready");
        readyButton.setFont(new Font(30));
        readyButton.setMaxWidth(300);


        Layout.getChildren().addAll(header, participants, readyButton);
        Scene lobbyScene = new Scene(Layout, configureScreenSize.getHeight() * size,configureScreenSize.getHeight() * size);

        LobbyStage.setScene(lobbyScene);
        LobbyStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
