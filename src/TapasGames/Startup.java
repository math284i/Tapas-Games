package TapasGames;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Startup extends Application {
    //STARTUP UI
    //CLIENT UI
    //SERVER UI
    private double height = 600;
    private double width = 400;

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UiFiles/frontpage.fxml"));

            Scene scene = new Scene(root);
            stage.setTitle("FXML Welcome");
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e);
        }
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
