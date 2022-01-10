package TapasGames.Chat.Game.MiniGames;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerMovement extends Application {

    private static final double W = 600, H = 600;
    private static final Logger _logger = Logger.getLogger(PlayerMovement.class.getName());

    private static final String PLAYER_IMAGE_LOC = "src/TapasGames/Ressources/player.png";
    private Image playerImage;
    private Node player;

    boolean run, goUp, goDown, goLeft, goRight;

    @Override
    public void start(Stage stage) throws Exception {
        playerImage = new Image(new FileInputStream(PLAYER_IMAGE_LOC));
        ImageView playerView = new ImageView(playerImage);
        playerView.setPreserveRatio(true);
        playerView.setFitHeight(100);
        playerView.setFitWidth(100);
        player = playerView;
        Group dungeon = new Group(player);

        movePlayerTo(W / 2, H / 2); //Move hero to center

        Scene scene = new Scene(dungeon, W, H, Color.FORESTGREEN);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> {
                        goUp = true;
                        _logger.log(Level.INFO, "Up!");
                    }
                    case DOWN -> goDown = true;
                    case LEFT -> goLeft = true;
                    case RIGHT -> goRight = true;
                    case SHIFT -> run = true;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> goUp = false;
                    case DOWN -> goDown = false;
                    case LEFT -> goLeft = false;
                    case RIGHT -> goRight = false;
                    case SHIFT -> run = false;
                }
            }
        });

        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                int dx = 0, dy = 0;

                if (goUp) dy -= 1;
                if (goDown) dy += 1;
                if (goRight) dx += 1;
                if (goLeft) dx -= 1;
                if (run) {dx *= 3; dy *= 3; }
                //_logger.log(Level.INFO, "MovingPlayerBy: " + dx + " : " + dy);
                movePlayerBy(dx, dy);
            }
        };
        timer.start();
    }

    private void movePlayerBy(int dx, int dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = player.getBoundsInLocal().getWidth() / 2;
        final double cy = player.getBoundsInLocal().getWidth() / 2;

        double x = cx + player.getLayoutX() + dx;
        double y = cy + player.getLayoutY() + dy;

        movePlayerTo(x, y);
    }

    private void movePlayerTo(double x, double y) {
        final double cx = player.getBoundsInLocal().getWidth() / 2;
        final double cy = player.getBoundsInLocal().getWidth() / 2;
        _logger.log(Level.INFO, "cx: " + cx + " x: " + x);
        _logger.log(Level.INFO, "cy: " + cy + " y: " + y);
        //Check bounderies
        if (x - cx >= 0 &&
            x + cx <= W &&
            y - cy >= 0 &&
            y + cy <= H) {
            _logger.log(Level.INFO, "moving!");
            player.relocate(x - cx, y - cy);
        }
        //_logger.log(Level.INFO, "MovingPlayerTo: " + (x - cx) + " : " + (y - cy));
    }

    public static void main(String[] args) { launch(args); }
}
