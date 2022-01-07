package TapasGames.UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
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
        Stage chatStage = new Stage();
        chatStage.setTitle("Chat Window");

        chatStage.setWidth(configureScreenSize.getWidth() * 0.7 * 0.25);
        chatStage.setHeight(configureScreenSize.getHeight() * 0.70);

        chatStage.setX(configureScreenSize.getWidth() * 0.15 + configureScreenSize.getWidth() * 0.7 * 0.75);
        chatStage.setY(configureScreenSize.getHeight() * 0.15);

        //Pane used for the layout
        BorderPane layout = new BorderPane();

        //<editor-fold desc="Code for: Global/Team Chat + header">
        VBox chatHeader = new VBox();
        chatHeader.setPadding(new Insets(10, 10, 10, 10));

        Label headerText = new Label("Messages");
        headerText.setFont(new Font("Arial", 30));
        chatHeader.setAlignment(Pos.TOP_CENTER);

        Button globalChat = new Button("global");
        Button teamChat = new Button("Team");

        HBox chatButtons = new HBox(globalChat, teamChat);
        chatButtons.setAlignment(Pos.TOP_CENTER);

        chatHeader.getChildren().addAll(headerText, chatButtons);

        layout.setTop(chatHeader);
        //</editor-fold>

        //<editor-fold desc="Code for: Messages sent to the chat">
        ScrollPane chatPane = new ScrollPane();
        chatPane.setPadding(new Insets(10, 10, 10, 10));


        layout.setCenter(chatPane);
        //</editor-fold>

        //<editor-fold desc="Code for: Sent Message Text-field/Button">
        //Make message send box
        VBox sentMessageBar = new VBox();
        sentMessageBar.setPadding(new Insets(10, 10, 10, 10));

        TextField messageBox = new TextField();
        messageBox.setPromptText("Write Message");
        messageBox.setPrefColumnCount(10);
        messageBox.getText();

        //Make send button
        Button sentButtons = new Button("Sent");

        sentMessageBar.getChildren().addAll(messageBox, sentButtons);

        layout.setBottom(sentMessageBar);

        //</editor-fold>   ded

        Scene chatScene = new Scene(layout);
        chatStage.setResizable(false);
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
