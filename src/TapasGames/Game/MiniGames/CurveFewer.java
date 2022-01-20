package TapasGames.Game.MiniGames;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Screen;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class CurveFewer {
    private static final Rectangle2D configureScreenSize = Screen.getPrimary().getBounds();
    private static final double size = 0.7;
    private static final Logger _logger = Logger.getLogger(CurveFewer.class.getName());
    private static final double W = 1008
            , H = 567;

    private static final String PLAYER1_IMAGE_LOC = "TapasGames/Ressources/Player1cf.png";
    private static final String PLAYER2_IMAGE_LOC = "TapasGames/Ressources/Player2cf.png";
    private static final String PLAYER3_IMAGE_LOC = "TapasGames/Ressources/Player3cf.png";
    private static final String PLAYER4_IMAGE_LOC = "TapasGames/Ressources/Player4cf.png";

    private double playerWidth = 25;
    private double playerHeight = 25;

    int playersAlive;
    public boolean GameOver;

    private HashMap<String, String> spawnPos;  //Player 1 = x,y ...
    private HashMap<String, Path> playersPath;
    private HashMap<String, Node> players;
    private HashMap<String, Double> playerAngels;

    public boolean goLeft, goRight;

    public CurveFewer(int playerAmount) {
        playersAlive = playerAmount;
        spawnPos = new HashMap<>();
        playersPath = new HashMap<>();
        players = new HashMap<>();
        playerAngels = new HashMap<>();
        SpawnSetup();
        PathSetup();
        PlayerSetup();
    }

    public void SpawnSetup() {
        spawnPos.put("1", "" + W/4 + "," + H/4);
        spawnPos.put("2", "" + W*3/4 + "," + H*3/4);
        spawnPos.put("3", "" + W/4 + "," + H*3/4);
        spawnPos.put("4", "" + W*3/4 + "," + H/4);

        playerAngels.put("1", 0.0);
        playerAngels.put("2", 0.0);
        playerAngels.put("3", 0.0);
        playerAngels.put("4", 0.0);
    }

    public void PathSetup() {
        Path path1 = new Path();
        path1.setStroke(Color.RED);
        path1.setStrokeWidth(2);
        Path path2 = new Path();
        path2.setStroke(Color.BLUE);
        path2.setStrokeWidth(2);
        Path path3 = new Path();
        path3.setStroke(Color.GREEN);
        path3.setStrokeWidth(2);
        Path path4 = new Path();
        path4.setStroke(Color.YELLOW);
        path4.setStrokeWidth(2);

        playersPath.put("1", path1);
        playersPath.put("2", path2);
        playersPath.put("3", path3);
        playersPath.put("4", path4);
    }

    public void PlayerSetup() {
        Image player1Image = new Image(PLAYER1_IMAGE_LOC);
        ImageView player1View = new ImageView(player1Image);
        player1View.setPreserveRatio(true);
        player1View.setFitHeight(playerHeight);
        player1View.setFitWidth(playerWidth);
        Node player1 = player1View;
        Image player2Image = new Image(PLAYER2_IMAGE_LOC);
        ImageView player2View = new ImageView(player2Image);
        player2View.setPreserveRatio(true);
        player2View.setFitHeight(playerHeight);
        player2View.setFitWidth(playerWidth);
        Node player2 = player2View;
        Image player3Image = new Image(PLAYER3_IMAGE_LOC);
        ImageView player3View = new ImageView(player3Image);
        player3View.setPreserveRatio(true);
        player3View.setFitHeight(playerHeight);
        player3View.setFitWidth(playerWidth);
        Node player3 = player3View;
        Image player4Image = new Image(PLAYER4_IMAGE_LOC);
        ImageView player4View = new ImageView(player4Image);
        player4View.setPreserveRatio(true);
        player4View.setFitHeight(playerHeight);
        player4View.setFitWidth(playerWidth);
        Node player4 = player4View;

        players.put("1", player1);
        players.put("2", player2);
        players.put("3", player3);
        players.put("4", player4);
    }

    public Scene start() throws Exception {
        //playerImage = new Image(new FileInputStream(PLAYER_IMAGE_LOC));
        Group dungeon = new Group();
        for (int i = 0; i < playersAlive; i++) {
            Node player = players.get(("" + (i+1)));
            Path path = playersPath.get(("" + (i+1)));
            String[] data = spawnPos.get(("" + (i+1))).split(",");
            double spawnX = Double.parseDouble(data[0]);
            double spawnY = Double.parseDouble(data[1]);
            dungeon.getChildren().add(player);
            dungeon.getChildren().add(path);
            movePlayerTo(player, spawnX, spawnY, 0, path);
        }

        Scene scene = new Scene(dungeon, W, H, Color.WHITE);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case LEFT -> goLeft = true;
                    case RIGHT -> goRight = true;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case LEFT -> goLeft = false;
                    case RIGHT -> goRight = false;
                }
            }
        });

        return scene;
    }

    public void UpdatePlayer(String playerNumber, boolean goLeft,  boolean goRight) {
        Node player = players.get(playerNumber);
        double angle = playerAngels.get(playerNumber);
        Path path = playersPath.get(playerNumber);

        double dx = 0, dy = 0;

        if (goRight) angle += 0.025;
        if (goLeft) angle -= 0.025;
        dy = Math.sin(angle);
        dx = Math.cos(angle);
        {dx *= 2; dy *= 2;}

        playerAngels.put(playerNumber, angle);

        player.setRotate(angle * (180/Math.PI));
        movePlayerBy(player, dx, dy, angle, path);
    }

    private void movePlayerBy(Node player, double dx, double dy, double angle, Path path) {
        if (dx == 0 && dy == 0) return;

        final double cx = player.getBoundsInLocal().getWidth() / 2;
        final double cy = player.getBoundsInLocal().getWidth() / 2;

        double x = cx + player.getLayoutX() + dx;
        double y = cy + player.getLayoutY() + dy;

        movePlayerTo(player, x, y, angle, path);
    }

    private void movePlayerTo(Node player, double x, double y, double angle, Path path) {
        final double cx = player.getBoundsInLocal().getWidth() / 2; //Gives the middle of the player,
        final double cy = player.getBoundsInLocal().getHeight() / 2;
        double a = (player.getBoundsInLocal().getWidth() / 2) + (player.getLayoutX() + Math.cos(angle)*8) + Math.cos(angle);
        double b = (player.getBoundsInLocal().getWidth() / 2) + (player.getLayoutY() + Math.sin(angle)*8) + Math.sin(angle);

        for (var entry: playersPath.entrySet()) {
            if (entry.getValue().contains(a - cx + playerWidth/2, b - cy + playerHeight/2)) {
                //Player dead!
                path.getElements().clear();
        }

        }
        if (x - cx < 0) player.relocate(W - playerWidth, y - cy);
        else if (x + cx > W) player.relocate(0, y - cy);
        else if (y - cy < 0) player.relocate(x - cx, H - playerHeight);
        else if (y + cy > H) player.relocate(x - cx, 0);

        else {
            player.relocate(x - cx, y - cy);
            path.getElements()
                    .add(new MoveTo(x - cx + playerWidth/2, y - cy + playerHeight/2));
            path.getElements().add(new LineTo(x - cx + playerWidth/2, y - cy + playerHeight/2));
        }
    }
}
