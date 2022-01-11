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
            FXMLLoader loader = FXMLLoader.load(getClass().getResource("/src/TapasGames/UiFiles/frontpage.fxml"));

            BorderPane root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("FXML Welcome");
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e);
        }
        stage.show();
        //What u wanna be?
        //If client
        //New page - Get name and IP - check if valid name
        //Repository
        //New ClientMain(NEW UICONTROLLER(CLIENT UI), name, ip, repository)

        //if server
        //New ServerMain(NEW UICONTROLLER(SERVER UI), repository)
    }

    public static void main(String[] args) {
        launch(args);
    }

}
