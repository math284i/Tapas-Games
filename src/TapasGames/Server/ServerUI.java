package TapasGames.Server;

import TapasGames.Client.ClientMain;
import javafx.application.Application;
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

    public ServerUI() {

    }

    @Override
    public void start(Stage stage) throws Exception {
        _clientList = new HashMap<>();
        try {
            Scene scene = FXMLLoader.load(getClass().getResource("../UiFiles/serverpage.fxml"));

            stage.setTitle("Server");
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e);
        }
        _stage = stage;
        stage.show();


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
        VBox clientsConnected = new VBox();

        VBox clients = new VBox(clientsS,sep2h,clientsConnected);

        TabPane chatTabs = new TabPane();
        chatTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox chatBoxT1 = new VBox();
        VBox chatBoxT2 = new VBox();
        VBox chatBoxG = new VBox();
        Tab globalChat = new Tab();
        Tab teamChat1 = new Tab();
        Tab teamChat2 = new Tab();

        ScrollPane chatPaneG = new ScrollPane();
        chatPaneG.setPadding(new Insets(10, 10, 10, 10));
        chatPaneG.setContent(chatBoxG);
        globalChat.setContent(chatPaneG);

        ScrollPane chatPaneT1 = new ScrollPane();
        chatPaneT1.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT1.setContent(chatBoxG);
        teamChat1.setContent(chatPaneT1);

        ScrollPane chatPaneT2 = new ScrollPane();
        chatPaneT2.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT2.setContent(chatBoxG);
        teamChat2.setContent(chatPaneT2);
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
        chatTabs.getTabs().addAll(globalChat,teamChat1,teamChat2);

        Separator sep1v = new Separator(Orientation.VERTICAL);
        HBox outerH = new HBox(5,status,sep1v,clients,chatTabs);

        Scene output = new Scene(outerH);
    }

    @FXML
    private VBox vboxClientList;

    public void AddClient(String clientName) {
        Label newName = new Label(clientName);
        _clientList.put(clientName, newName);
        vboxClientList.getChildren().add(newName);
    }

    public void RemoveClient(String clientName) {

    }

}
