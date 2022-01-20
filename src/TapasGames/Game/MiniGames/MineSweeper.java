package TapasGames.Game.MiniGames;

import java.util.HashMap;
import java.util.Random;

import TapasGames.UI.UIController;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


class Field {
    private boolean mine;
    private boolean pressed;

    public Field() {
        this.mine = false;
        this.pressed = false;
    }

    public void setMine() {
        this.mine = !this.mine;
    }

    public boolean isMine() {
        return this.mine;
    }

    public void setPressed() {
        this.pressed = true;
    }

    public boolean isPressed() {
        return this.pressed;
    }

}
