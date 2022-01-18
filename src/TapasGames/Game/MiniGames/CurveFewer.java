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
import java.util.logging.Logger;

public class CurveFewer {
    private static final Rectangle2D configureScreenSize = Screen.getPrimary().getBounds();
    private static final double size = 0.7;
    private static final Logger _logger = Logger.getLogger(CurveFewer.class.getName());
    private static final double W = configureScreenSize.getWidth() * size * 0.75
            , H = configureScreenSize.getHeight() * size * 0.75;

    //private static final String PLAYER_IMAGE_LOC = "src/TapasGames/Ressources/player.png";
    private static final String PLAYER_IMAGE_LOC = "/Users/dyberg/Desktop/DTU/02148/Tapas-Games/src/TapasGames/Ressources/player.png";
    private Image playerImage;
    private Node player;
    private double angle = 0;
    private double playerWidth = 25;
    private double playerHeight = 25;

    private Path path = new Path();
    private Color pathColor = Color.BLACK;
    private double pathSize = 2;

    boolean goLeft, goRight;

    private AnimationTimer timer;
    //private Stage stage;

    public void stop() {
        timer.stop();
        //stage.close();
    }

    public Scene start() throws Exception {
        playerImage = new Image(new FileInputStream(PLAYER_IMAGE_LOC));
        ImageView playerView = new ImageView(playerImage);
        playerView.setPreserveRatio(true);
        playerView.setFitHeight(playerHeight);
        playerView.setFitWidth(playerWidth);
        player = playerView;
        Group dungeon = new Group(player);

        movePlayerTo(W / 2, H / 2); //Move hero to center

        Scene scene = new Scene(dungeon, W, H, Color.WHITE);

        path.setStrokeWidth(pathSize);
        path.setStroke(pathColor);

        dungeon.getChildren().add(path);

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

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                double dx = 0, dy = 0;

                if (goRight) angle += 0.025;
                if (goLeft) angle -= 0.025;
                dy = Math.sin(angle);
                dx = Math.cos(angle);

                movePlayerBy(dx, dy);
            }
        };
        timer.start();
        this.timer = timer;
        return scene;
    }

    private void movePlayerBy(double dx, double dy) {
        if (dx == 0 && dy == 0) return;

        final double cx = player.getBoundsInLocal().getWidth() / 2;
        final double cy = player.getBoundsInLocal().getWidth() / 2;

        double x = cx + player.getLayoutX() + dx;
        double y = cy + player.getLayoutY() + dy;

        movePlayerTo(x, y);
    }

    private void movePlayerTo(double x, double y) {
        final double cx = player.getBoundsInLocal().getWidth() / 2; //Gives the middle of the player,
        final double cy = player.getBoundsInLocal().getHeight() / 2;
        double a = (player.getBoundsInLocal().getWidth() / 2) + (player.getLayoutX() + Math.cos(angle)*8) + Math.cos(angle);
        double b = (player.getBoundsInLocal().getWidth() / 2) + (player.getLayoutY() + Math.sin(angle)*8) + Math.sin(angle);

        if (path.contains(a - cx + playerWidth/2, b - cy + playerHeight/2)) {
            path.getElements().clear();

        }
        if (x - cx < 0) player.relocate(W - playerWidth, y - cy);
        else if (x + cx > W) player.relocate(0, y - cy);
        else if (y - cy < 0) player.relocate(x - cx, H - playerHeight);
        else if (y + cy > H) player.relocate(x - cx, 0);

        else {
            player.relocate(x - cx, y - cy);
            path.setStroke(pathColor);
            path.getElements()
                    .add(new MoveTo(x - cx + playerWidth/2, y - cy + playerHeight/2));
            path.getElements().add(new LineTo(x - cx + playerWidth/2, y - cy + playerHeight/2));
        }
    }
}
