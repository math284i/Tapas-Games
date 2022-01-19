package TapasGames.UiFiles;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class VotingUI {

    public Scene start() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../UiFiles/voting.fxml"));
            return new Scene(root);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
