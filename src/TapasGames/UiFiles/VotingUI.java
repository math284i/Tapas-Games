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

    public Scene start() {
        Label title = new Label("Vote for the game you want to play!");
        title.setFont(new Font(19));

        ToggleGroup tg = new ToggleGroup();
        RadioButton stop = new RadioButton("Stop");
        RadioButton ms = new RadioButton("Minesweeper");
        RadioButton cv = new RadioButton("Curvefever");
        stop.setToggleGroup(tg);
        ms.setToggleGroup(tg);
        cv.setToggleGroup(tg);
        stop.setSelected(true);
        HBox hbox = new HBox(20,stop,ms,cv);
        hbox.setAlignment(Pos.CENTER);

        Button ok = new Button("OK");
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

        VBox vbox = new VBox(title,hbox,ok);
        vbox.setAlignment(Pos.CENTER);

        return new Scene(vbox);
    }

}
