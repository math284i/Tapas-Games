package TapasGames.UI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GameUI extends Application {

    Rectangle2D configureScreenSize;

    @Override
    public void start(Stage stage) throws Exception{
        configureScreenSize = Screen.getPrimary().getBounds();

        GameScene();
        MenuScene();
        ChatScene();

    }

    public void GameScene(){
        Stage gameScene = new Stage();
        gameScene.setTitle("Game Window");

        gameScene.setWidth(configureScreenSize.getWidth() * 0.7 * 0.75);
        gameScene.setHeight(configureScreenSize.getHeight() * 0.7 * 0.75);

        gameScene.setX(configureScreenSize.getWidth() * 0.15);
        gameScene.setY(configureScreenSize.getHeight() * 0.15);

        gameScene.setResizable(false);

        gameScene.show();
    }

    public void MenuScene(){
        Stage menuScene = new Stage();
        menuScene.setTitle("Menu Window");

        menuScene.setWidth(configureScreenSize.getWidth() * 0.7 * 0.75);
        menuScene.setHeight(configureScreenSize.getHeight() * 0.7 * 0.25);

        menuScene.setX(configureScreenSize.getWidth() * 0.15);
        menuScene.setY(configureScreenSize.getHeight() * 0.15 + configureScreenSize.getHeight() * 0.7 * 0.75);

        menuScene.setResizable(false);

        menuScene.show();
    }

    public void ChatScene(){
        Stage chatScene = new Stage();
        chatScene.setTitle("Chat Window");

        chatScene.setWidth(configureScreenSize.getWidth() * 0.7 * 0.25);
        chatScene.setHeight(configureScreenSize.getHeight() * 0.70);

        chatScene.setX(configureScreenSize.getWidth() * 0.15 + configureScreenSize.getWidth() * 0.7 * 0.75);
        chatScene.setY(configureScreenSize.getHeight() * 0.15);

        chatScene.setResizable(false);

        chatScene.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
