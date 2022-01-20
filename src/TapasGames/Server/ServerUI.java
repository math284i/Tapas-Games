package TapasGames.Server;

import TapasGames.Client.ClientMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerUI extends Application {
    private Stage _stage;
    private HashMap<String, Label> _clientList;
    public VBox _clientConnected;
    public TabPane _chatTabs;
    public Tab _globalChat;
    public Tab _team1Chat;
    public Tab _team2Chat;

    public VBox chatBoxT1;
    public VBox chatBoxT2;
    public VBox chatBoxG;

    public ServerUI() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        _clientList = new HashMap<>();




        Label statusL = new Label("Status:");
        statusL.setAlignment(Pos.CENTER);
        statusL.setFont(new Font(20));
        Separator sep1h = new Separator(Orientation.HORIZONTAL);
        Pane statusP = new Pane();
        statusP.setStyle("-fx-border-color: black;");
        Label online = new Label("Server online");
        online.setFont(new Font(18));
        online.setAlignment(Pos.CENTER);
        statusP.getChildren().add(online);
        VBox status = new VBox(statusL,sep1h,statusP);

        Label clientsS = new Label("Client list");
        clientsS.setFont(new Font(20));
        clientsS.setAlignment(Pos.CENTER);
        Separator sep2h = new Separator(Orientation.HORIZONTAL);
        _clientConnected = new VBox();

        VBox clients = new VBox(clientsS, sep2h, _clientConnected);

        _chatTabs = new TabPane();
        _chatTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        chatBoxT1 = new VBox();
        chatBoxT2 = new VBox();
        chatBoxG = new VBox();
        _globalChat = new Tab();
        _team1Chat = new Tab();
        _team2Chat = new Tab();

        ScrollPane chatPaneG = new ScrollPane();
        chatPaneG.setPadding(new Insets(10, 10, 10, 10));
        chatPaneG.setContent(chatBoxG);
        _globalChat.setContent(chatPaneG);

        ScrollPane chatPaneT1 = new ScrollPane();
        chatPaneT1.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT1.setContent(chatBoxG);
        _team1Chat.setContent(chatPaneT1);

        ScrollPane chatPaneT2 = new ScrollPane();
        chatPaneT2.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT2.setContent(chatBoxG);
        _team2Chat.setContent(chatPaneT2);
        chatBoxT1.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneT1.setVvalue((Double)newValue );
            }
        });
        chatBoxT2.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneT2.setVvalue((Double)newValue );
            }
        });
        chatBoxG.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneG.setVvalue((Double) newValue);
            }
        });
        //chatTabs.getTabs().addAll(globalChat,teamChat1,teamChat2);

        Separator sep1v = new Separator(Orientation.VERTICAL);
        HBox outerH = new HBox(5, status, sep1v, clients, _chatTabs);

        Scene output = new Scene(outerH, 480, 640);
        _stage = new Stage();
        stage.setScene(output);
        stage.show();
    }

    public void AddClient(String clientName) {
        Platform.runLater(() -> {
            Label newName = new Label(clientName);
            _clientList.put(clientName, newName);
            _clientConnected.getChildren().add(newName);
        });
    }

    public void RemoveClient(String clientName) {
        _clientList.remove(clientName);
        //_clientConnected.getChildren().remove();

    }

    public void AddChat(String id) {
        Platform.runLater(() -> {
            switch (id) {
                case "Team 1" -> {
                    if (!_chatTabs.getTabs().contains(_team1Chat)) _chatTabs.getTabs().add(_team1Chat);
                }
                case "Team 2" -> {
                    if (!_chatTabs.getTabs().contains(_team2Chat)) _chatTabs.getTabs().add(_team2Chat);
                }
                case "Global" -> {
                    if (!_chatTabs.getTabs().contains(_globalChat)) _chatTabs.getTabs().add(_globalChat);
                }
            }

        });
    }

    public void UpdateChat(String id, String name, String message) {
        Platform.runLater(() -> {
            System.out.println("ServerUI: " + id);
            switch (id) {
                case "Team 1" -> {
                    Platform.runLater( () -> {
                        chatBoxT1.getChildren().add(new Label(name + " : " + message));
                    });
                }
                case "Team 2" -> {
                    Platform.runLater( () -> {
                        chatBoxT2.getChildren().add(new Label(name + " : " + message));
                    });
                }
                case "Global" -> {
                    Platform.runLater( () -> {
                        chatBoxG.getChildren().add(new Label(name + " : " + message));
                    });
                }
            }

        });
    }

}
