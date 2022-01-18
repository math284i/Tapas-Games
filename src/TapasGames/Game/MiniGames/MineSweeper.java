package TapasGames.Game.MiniGames;

import java.util.Random;

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


public class MineSweeper {

    private int[] size = { 16, 16 };
    private int mines = 51;
    private String[] colors  = { "whitesmoke", "teal", "green", "maroon", "navy", "brown", "cyan", "black", "grey" };
    private Label minesLeft = new Label();
    private Board board = new Board(size[0], size[1], mines);
    private List<List<Button>> buttons = new ArrayList<List<Button>>();
    private GridPane root = new GridPane();

    private int currentPlayer = 1;
    private int foundP1 = 0;
    private int foundP2 = 0;
    private Label player1 = new Label();
    private Label player2 = new Label();
    private Rectangle2D configureScreenSize;
    private int minesCurrent = mines;

    public Scene start() {
        configureScreenSize = Screen.getPrimary().getBounds();
        HBox info = new HBox(minesLeft, player1, player2);
        info.setStyle("-fx-background-color: lightgray");
        minesLeft.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(minesLeft, Priority.ALWAYS);
        player1.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(player1, Priority.ALWAYS);
        player2.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(player2, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + this.colors[0]);
        VBox vbox = new VBox(info,root);
        vbox.setFillWidth(true);
        VBox.setVgrow(root, Priority.ALWAYS);
        restart();

        return new Scene(vbox);
    }

    public void restart() {
        minesCurrent = mines;
        foundP1 = 0;
        foundP2 = 0;
        buttons.clear();
        minesLeft.setText("Mines left: " + mines);
        player1.setText("-> Player 1: " + foundP1);
        player2.setText("Player 2: " + foundP2);
        for (int i = 0; i < size[0]; ++i) {
            buttons.add(new ArrayList<Button>());
            for (int j = 0; j < size[1]; ++j) {
                Button button = new Button("  ");
                button.setId(String.valueOf(i) + "," + j);
                button.setOnMousePressed(this::mouseAction);
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

    public void mouseAction(final MouseEvent event) {
        String text = ((Button)event.getSource()).getId();
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
        int x = Integer.parseInt(xS);
        int y = Integer.parseInt(yS);
        if (event.getButton().equals((Object)MouseButton.PRIMARY)) {
            checkMines(x, y,true);
        }
        if(foundP1 == mines/2+1 || foundP2 == mines/2+1){
            final Stage settingsDialog = new Stage();
            settingsDialog.initModality(Modality.APPLICATION_MODAL);
            VBox content = new VBox();
            content.getChildren().add(new Label("Ulrik dum"));
            Scene settingsScene = new Scene(content,configureScreenSize.getWidth()/4,configureScreenSize.getHeight()/2);
            settingsDialog.setScene(settingsScene);
            settingsDialog.setResizable(false);
            settingsDialog.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue)
                    settingsDialog.setMaximized(false);
            });
            settingsDialog.show();
        }
    }

    public void checkMines(final int x, final int y, boolean firstClick) {
        if (!board.getGrid(x, y).isPressed()) {
            board.getGrid(x, y).setPressed();
            if (board.getGrid(x, y).isMine()) {
                minesCurrent --;
                minesLeft.setText("Mines left: " + minesCurrent);
                if(currentPlayer == 1){
                    foundP1 ++;
                    player1.setText("-> Player 1: " + foundP1);
                    buttons.get(x).get(y).setStyle("-fx-background-color: red");
                }else{
                    foundP2++;
                    player2.setText("-> Player 2: " + foundP2);
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
                    player2.setText("-> Player 2: " + foundP2);
                    player1.setText("Player 1: " + foundP1);
                }else if(firstClick){
                    currentPlayer = 1;
                    player1.setText("-> Player 1: " + foundP1);
                    player2.setText("Player 2: " + foundP2);
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
                    player2.setText("-> Player 2: " + foundP2);
                    player1.setText("Player 1: " + foundP1);
                }else if(firstClick){
                    currentPlayer = 1;
                    player1.setText("-> Player 1: " + foundP1);
                    player2.setText("Player 2: " + foundP2);
                }
            }
        }
    }
}

class Board {
    private Field[][] grid;
    private int mines;

    public Board(int sizeX, int sizeY, int mines) {
        if (sizeX < 1 || sizeY < 1 || mines < 0 || sizeX * sizeY < mines) {
            throw new IllegalArgumentException("Size and mines aren't properly defined");
        }
        this.mines = mines;
        this.grid = new Field[sizeX][sizeY];
        for (int i = 0; i < sizeX; ++i) {
            for (int j = 0; j < sizeY; ++j) {
                this.grid[i][j] = new Field();
            }
        }
        for (int i = 0; i < mines; ++i) {
            Random r;
            int r2;
            int r3;
            for (r = new Random(), r2 = r.nextInt(sizeX), r3 = r.nextInt(sizeY); this.grid[r2][r3].isMine(); r2 = r.nextInt(sizeX), r3 = r.nextInt(sizeY)) {}
            this.grid[r2][r3].setMine();
        }
    }

    public int neighbors(final int posX, final int posY) {
        int temp = 0;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                if ((i != 0 || j != 0) && posX + i >= 0 && posY + j >= 0 && posX + i < this.grid.length && posY + j < this.grid[0].length && this.grid[posX + i][posY + j].isMine()) {
                    ++temp;
                }
            }
        }
        return temp;
    }

    public Field getGrid(final int x, final int y) {
        if (x > this.grid.length || y > this.grid[0].length) {
            return null;
        }
        return this.grid[x][y];
    }
}

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
