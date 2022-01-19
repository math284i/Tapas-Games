package TapasGames.UiFiles;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class VotingUI {

    public Button btnOk;
    public ToggleGroup tgGames;

    public Scene start() {
        Label title = new Label("Vote for the game you want to play!");
        title.setFont(new Font(19));

        tgGames = new ToggleGroup();
        RadioButton stop = new RadioButton("Stop");
        RadioButton ms = new RadioButton("Minesweeper");
        RadioButton cv = new RadioButton("Curvefever");
        stop.setToggleGroup(tgGames);
        ms.setToggleGroup(tgGames);
        cv.setToggleGroup(tgGames);
        stop.setSelected(true);
        HBox hbox = new HBox(20, stop, ms, cv);
        hbox.setAlignment(Pos.CENTER);

        btnOk = new Button("OK");

        VBox vbox = new VBox(70, title, hbox, btnOk);
        vbox.setAlignment(Pos.CENTER);

        return new Scene(vbox);
    }

}
