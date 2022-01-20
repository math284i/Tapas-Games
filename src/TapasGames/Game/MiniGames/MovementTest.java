package TapasGames.Game.MiniGames;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovementTest extends Application {
    Path path;
    public double mouseX;
    public double mouseY;
    public String mouseClicked;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250);

        path = new Path();
        path.setStrokeWidth(1);
        path.setStroke(Color.BLACK);

        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMousePressed(mouseHandler);

        root.getChildren().add(path);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
            mouseClicked = "1";
            System.out.println("Mouse is clicked!: " + mouseX + " : " + mouseY);
            }
    };

    public static class MineSweeper {

        private int[] size = { 16, 16 };
        private int mines = 51;
        private String[] colors  = { "whitesmoke", "teal", "green", "maroon", "navy", "brown", "cyan", "black", "grey" };
        private Label minesLeft = new Label();
        //private Board board = new Board(size[0], size[1], mines);
        private Board board;
        private List<List<Button>> buttons = new ArrayList<List<Button>>();
        private GridPane root = new GridPane();
        private HashMap<String, Integer> _teamDic;

        private int currentPlayer = 1;
        private int foundP1 = 0;
        private int foundP2 = 0;
        private Label team1 = new Label();
        private Label team2 = new Label();
        private Rectangle2D configureScreenSize;
        private int minesCurrent = mines;

        public boolean GameOver = false;
        public String playersWon;

        public Scene _scene;
        public int mouseX = -10;
        public int mouseY = -10;
        public String mouseClicked = "0";

        public MineSweeper(int playerAmount) {
            _teamDic = new HashMap<>();

            switch (playerAmount) {
                case 1 -> {
                    GameOver = true;
                    playersWon = "Invalid";
                }
                case 2 -> {
                    _teamDic.put("1", 1); //Player1 is Team1
                    _teamDic.put("2", 2); //Player2 is Team2
                }
                case 3 -> {
                    //Could be random
                    _teamDic.put("1", 1); //Player 1 is Team 1
                    _teamDic.put("2", 2); //Player 2 is Team 2
                    _teamDic.put("3", 1); //Player 3 is Team 1
                }
                case 4 -> {
                    //Could be random
                    _teamDic.put("1", 1); //Player 1 is Team 1
                    _teamDic.put("2", 2); //Player 2 is Team 2
                    _teamDic.put("3", 1); //Player 3 is Team 1
                    _teamDic.put("4", 2); //Player 4 is Team 2
                }
            }

        }

        public Scene start(Board game) {
            configureScreenSize = Screen.getPrimary().getBounds();
            HBox info = new HBox(minesLeft, team1, team2);
            info.setStyle("-fx-background-color: lightgray");
            minesLeft.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(minesLeft, Priority.ALWAYS);
            team1.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(team1, Priority.ALWAYS);
            team2.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(team2, Priority.ALWAYS);
            root.setStyle("-fx-background-color: " + this.colors[0]);
            VBox vbox = new VBox(info,root);
            vbox.setFillWidth(true);
            VBox.setVgrow(root, Priority.ALWAYS);
            restart(game);
            _scene = new Scene(vbox);
            return _scene;
        }

        public void mouseHandler(MouseEvent mouseEvent) {
            String text = ((Button)mouseEvent.getSource()).getId();
            int n = 0;
            String xS = "";
            String yS = "";
            while (text.charAt(n) != ',') {
                xS = String.valueOf(xS) + text.charAt(n);
                ++n;
            }
            for (int i = n + 1; i < text.length(); ++i) {
                yS = String.valueOf(yS) + text.charAt(i);
            }
            mouseX = Integer.parseInt(xS);
            mouseY = Integer.parseInt(yS);

            mouseClicked = "1";
            System.out.println("Mouse is clicked!: " + mouseX + " : " + mouseY);
        }

        public void restart(Board game) {
            this.board = game;
            minesCurrent = mines;
            foundP1 = 0;
            foundP2 = 0;
            buttons.clear();
            minesLeft.setText("Mines left: " + mines);
            team1.setText("-> Team 1: " + foundP1);
            team2.setText("Team 2: " + foundP2);
            for (int i = 0; i < size[0]; ++i) {
                buttons.add(new ArrayList<Button>());
                for (int j = 0; j < size[1]; ++j) {
                    Button button = new Button("  ");
                    button.setId(String.valueOf(i) + "," + j);
                    button.setStyle("-fx-background-color: gray");
                    button.setOnMousePressed(this::mouseHandler);
                    root.add(button, i, j);
                    button.setPrefSize(configureScreenSize.getWidth()/size[0],configureScreenSize.getHeight()/size[1]);
                    GridPane.setHgrow(button, Priority.ALWAYS);
                    GridPane.setVgrow(button, Priority.ALWAYS);
                    GridPane.setHalignment(button, HPos.CENTER);
                    GridPane.setMargin(button, new Insets(1.0, 1.0, 1.0, 1.0));
                    buttons.get(i).add(button);
                }
            }
        }

        public boolean mouseAction(String playerNumber, boolean clicked, int x, int y) {
            if (currentPlayer != _teamDic.get(playerNumber)) return false;

            if (clicked) checkMines(x, y,true);
            else return false;

            if(foundP1 == mines/2+1){
                GameOver = true;
                playersWon = "";

                for (var entry: _teamDic.entrySet()) {
                    if (entry.getValue() == 1) {
                        playersWon += entry.getKey() + ":";
                    }
                }

            }
            else if (foundP2 == mines/2+1) {
                GameOver = true;
                playersWon = "";

                for (var entry: _teamDic.entrySet()) {
                    if (entry.getValue() == 2) {
                        playersWon += entry.getKey() + ":";
                    }
                }
            }

            return true;
        }

        public void checkMines(final int x, final int y, boolean firstClick) {
            System.out.println("CheckMines: " + x + " : " + y);
            if (!board.getGrid(x, y).isPressed()) {
                board.getGrid(x, y).setPressed();
                if (board.getGrid(x, y).isMine()) {
                    minesCurrent --;
                    minesLeft.setText("Mines left: " + minesCurrent);
                    if(currentPlayer == 1){
                        foundP1 ++;
                        team1.setText("-> Player 1: " + foundP1);
                        buttons.get(x).get(y).setStyle("-fx-background-color: red");
                    }else{
                        foundP2++;
                        team2.setText("-> Player 2: " + foundP2);
                        buttons.get(x).get(y).setStyle("-fx-background-color: blue");
                    }
                }
                else if (board.neighbors(x, y) > 0) {
                    for (int i = 1; i <= 8; ++i) {
                        if (board.neighbors(x, y) == i) {
                            buttons.get(x).get(y).setStyle("-fx-background-color: silver; -fx-text-fill: " + colors[i]);
                        }
                    }
                    buttons.get(x).get(y).setText(new StringBuilder().append(board.neighbors(x, y)).toString());
                    if(currentPlayer == 1 && firstClick){
                        currentPlayer = 2;
                        team2.setText("-> Player 2: " + foundP2);
                        team1.setText("Player 1: " + foundP1);
                    }else if(firstClick){
                        currentPlayer = 1;
                        team1.setText("-> Player 1: " + foundP1);
                        team2.setText("Player 2: " + foundP2);
                    }
                }
                else {
                    buttons.get(x).get(y).setStyle("-fx-background-color: " + colors[0]);
                    for (int i = -1; i <= 1; ++i) {
                        for (int j = -1; j <= 1; ++j) {
                            if ((i != 0 || j != 0) && x + i >= 0 && y + j >= 0 && x + i < buttons.size() && y + j < buttons.get(0).size() && !board.getGrid(x + i, y + j).isPressed()) {
                                checkMines(x + i, y + j,false);
                            }
                        }
                    }
                    if(currentPlayer == 1 && firstClick){
                        currentPlayer = 2;
                        team2.setText("-> Player 2: " + foundP2);
                        team1.setText("Player 1: " + foundP1);
                    }else if(firstClick){
                        currentPlayer = 1;
                        team1.setText("-> Player 1: " + foundP1);
                        team2.setText("Player 2: " + foundP2);
                    }
                }
            }
        }
    }
}