package TapasGames.Server;

import TapasGames.Client.ClientMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerUI extends Application {
    private Stage _stage;

    @Override
    public void start(Stage stage) throws Exception {
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

    public void AddClient(String clientName) {

    }

    public void RemoveClient(String clientName) {

    }

}
