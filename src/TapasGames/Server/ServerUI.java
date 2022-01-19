package TapasGames.Server;

import TapasGames.Client.ClientMain;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
