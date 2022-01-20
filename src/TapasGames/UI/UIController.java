package TapasGames.UI;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import TapasGames.Game.MiniGames.Board;
import TapasGames.Game.MiniGames.CurveFewer;
import TapasGames.Game.MiniGames.MineSweeper;
import TapasGames.UiFiles.LobbyUI;
import TapasGames.UiFiles.VotingUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.text.Font;
import javafx.stage.*;

import java.io.FileNotFoundException;


public class UIController extends Application {

    Rectangle2D configureScreenSize;
    double size = 0.7;
    Stage gameStage;
    Stage menuStage;
    Stage chatStage;
    VotingUI votingWindow;

    public String _playerName;
    public String _playerNumber = "0";

    int score1 = 0;
    int score2 = 0;

    TabPane chatTabs;
    ScrollPane chatPaneG;
    ScrollPane chatPaneT1;
    ScrollPane chatPaneT2;
    VBox chatBoxG;
    VBox chatBoxT1;
    VBox chatBoxT2;
    Tab teamChat1 = new Tab("Team 1");
    Tab teamChat2 = new Tab("Team 2");
    Tab globalChat = new Tab("Global");
    Label team1L;
    Label team2L;
    Label team3L;
    Label team4L;

    ImageView controls = new ImageView();
    Image minesweeperI;
    Image curvefeverI;

    public String _gameScene = "lobby";

    private SequentialSpace _clientSpace;

    public LobbyUI lobbyGame;
    public MineSweeper minesweeperGame;
    public Scene minesweeperScene;
    public CurveFewer curvefeverGame;

    public UIController() {

    }

    public UIController(SequentialSpace clientSpace) {
        _clientSpace = clientSpace;

        new Thread(new ClientReceiver(this, _clientSpace)).start();
    }

    @Override
    public void start(Stage stage) throws Exception {
        configureScreenSize = Screen.getPrimary().getBounds();

        GameScene();
        MenuScene();
        ChatScene();
        gameStage.toFront();
    }

    public void start(Stage stage, SequentialSpace clientSpace) throws Exception {
        configureScreenSize = Screen.getPrimary().getBounds();
        _clientSpace = clientSpace;

        GameScene();
        MenuScene();
        ChatScene();
        gameStage.toFront();
    }

    public void GameScene() throws Exception {
        gameStage = new Stage();
        gameStage.setTitle("Game Window");

        gameStage.setWidth(configureScreenSize.getWidth() * size * 0.75);
        gameStage.setHeight(configureScreenSize.getHeight() * size * 0.75);

        gameStage.setX(configureScreenSize.getWidth() * 0.15);
        gameStage.setY(configureScreenSize.getHeight() * 0.15);

        gameStage.setResizable(false);

        updateGameScene(_gameScene, "0", "1");

        gameStage.show();
    }

    //TODO: Create an update function for the GameScene.

    public void MenuScene() throws FileNotFoundException {
        menuStage = new Stage();
        menuStage.setTitle("Menu Window");
        menuStage.setWidth(configureScreenSize.getWidth() * size * 0.75);
        menuStage.setHeight(configureScreenSize.getHeight() * size * 0.25);

        menuStage.setX(configureScreenSize.getWidth() * 0.15);
        menuStage.setY(configureScreenSize.getHeight() * 0.15 + configureScreenSize.getHeight() * size * 0.75);

        menuStage.initOwner(gameStage);
        menuStage.setResizable(false);
        menuStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
        });

        GridPane buttons = new GridPane();
        Button exit = new Button("Exit");
        Button vts = new Button("Vote to Skip");
        Separator separatorH = new Separator(Orientation.HORIZONTAL);
        Separator separatorV = new Separator(Orientation.VERTICAL);
        Separator separatorV1 = new Separator(Orientation.VERTICAL);
        Separator separatorV2 = new Separator(Orientation.VERTICAL);
        Separator separatorV3 = new Separator(Orientation.VERTICAL);
        VBox team1 = new VBox();
        team1L = new Label("Player 1");
        Label team1S = new Label("" + score1);
        VBox team2 = new VBox();
        team2L = new Label("Player 2");
        Label team2S = new Label("" + score2);
        VBox team3 = new VBox();
        team3L = new Label("Player 3");
        Label team3S = new Label("0");
        VBox team4 = new VBox();
        team4L = new Label("Player 4");
        Label team4S = new Label("0");
        HBox teams = new HBox();
        Label points = new Label("Points");
        VBox score = new VBox();

        exit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        vts.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttons.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttons.add(exit, 0, 0, 1, 2);
        buttons.add(vts, 1, 0, 1, 1);
        buttons.setVgrow(exit, Priority.ALWAYS);
        buttons.setVgrow(vts, Priority.ALWAYS);
        buttons.setHgrow(exit, Priority.ALWAYS);
        buttons.setHgrow(vts, Priority.ALWAYS);
        buttons.setVgap(5);
        buttons.setHgap(5);
        buttons.setPrefSize(configureScreenSize.getWidth() * size * 0.25, configureScreenSize.getHeight() * size * 0.2);

        team1.setVgrow(team1L, Priority.NEVER);
        team1.setVgrow(team1S, Priority.ALWAYS);
        team1L.setMaxWidth(Double.MAX_VALUE);
        team1S.setMaxWidth(Double.MAX_VALUE);
        team1S.setMaxHeight(Double.MAX_VALUE);
        team1L.setAlignment(Pos.TOP_CENTER);
        team1S.setAlignment(Pos.CENTER);
        team1.getChildren().addAll(team1L, team1S);

        team2.setVgrow(team2L, Priority.NEVER);
        team2.setVgrow(team2S, Priority.ALWAYS);
        team2L.setMaxWidth(Double.MAX_VALUE);
        team2S.setMaxWidth(Double.MAX_VALUE);
        team2S.setMaxHeight(Double.MAX_VALUE);
        team2L.setAlignment(Pos.TOP_CENTER);
        team2S.setAlignment(Pos.CENTER);
        team2.getChildren().addAll(team2L, team2S);

        team3.setVgrow(team3L, Priority.NEVER);
        team3.setVgrow(team3S, Priority.ALWAYS);
        team3L.setMaxWidth(Double.MAX_VALUE);
        team3S.setMaxWidth(Double.MAX_VALUE);
        team3S.setMaxHeight(Double.MAX_VALUE);
        team3L.setAlignment(Pos.TOP_CENTER);
        team3S.setAlignment(Pos.CENTER);
        team3.getChildren().addAll(team3L, team3S);

        team4.setVgrow(team4L, Priority.NEVER);
        team4.setVgrow(team4S, Priority.ALWAYS);
        team4L.setMaxWidth(Double.MAX_VALUE);
        team4S.setMaxWidth(Double.MAX_VALUE);
        team4S.setMaxHeight(Double.MAX_VALUE);
        team4L.setAlignment(Pos.TOP_CENTER);
        team4S.setAlignment(Pos.CENTER);
        team4.getChildren().addAll(team4L, team4S);

        teams.setHgrow(team1, Priority.ALWAYS);
        teams.setHgrow(team2, Priority.ALWAYS);
        teams.setHgrow(team3, Priority.ALWAYS);
        teams.setHgrow(team4, Priority.ALWAYS);
        team1.setMaxHeight(Double.MAX_VALUE);
        team2.setMaxHeight(Double.MAX_VALUE);
        team3.setMaxHeight(Double.MAX_VALUE);
        team4.setMaxHeight(Double.MAX_VALUE);
        teams.getChildren().addAll(team1, separatorV1, team2);
        teams.getChildren().addAll(separatorV2,team3); //Adds team 3
        teams.getChildren().addAll(separatorV3,team4); //Adds team 4

        score.setVgrow(teams, Priority.ALWAYS);
        score.setVgrow(points, Priority.NEVER);
        teams.setMaxWidth(Double.MAX_VALUE);
        points.setMaxWidth(Double.MAX_VALUE);
        points.setAlignment(Pos.TOP_CENTER);
        score.getChildren().addAll(points, separatorH, teams);
        score.setPrefSize(configureScreenSize.getWidth() * size * 0.25, configureScreenSize.getHeight() * size * 0.2);

        StackPane con = new StackPane();
        //con.setPrefSize(configureScreenSize.getWidth()*size*0.25,configureScreenSize.getHeight()*size*0.2);
        con.setMinWidth(configureScreenSize.getWidth() * size * 0.25);
        con.setMinHeight(configureScreenSize.getHeight() * size * 0.2);
        con.setAlignment(Pos.CENTER);
        controls.setFitWidth(configureScreenSize.getWidth() * size * 0.25);
        controls.setFitHeight(configureScreenSize.getHeight() * size * 0.2);
        //Image minesweeperI = new Image(new FileInputStream("src/TapasGames/Ressources/MineSweeperControls.png"));
        minesweeperI = new Image("TapasGames/Ressources/MineSweeperControls.png");
        curvefeverI = new Image("TapasGames/Ressources/CurveFeverControls.png");
        //controls.setImage(minesweeperI);
        controls.setPreserveRatio(true);
        controls.setSmooth(true);
        controls.setCache(true);
        con.getChildren().add(controls);

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                curvefeverGame.stop();
                gameStage.close();
                Stage stage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("../UiFiles/frontpage.fxml"));

                    Scene scene = new Scene(root);
                    stage.setTitle("FXML Welcome");
                    stage.setScene(scene);
                } catch (Exception e) {
                    System.out.println(e);
                }
                stage.show();
            }
        });
        vts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    _clientSpace.put("UIToClient", "rockTheVote", "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        HBox combi = new HBox();
        combi.getChildren().addAll(buttons, con, separatorV, score);
        Scene menuScene = new Scene(combi, configureScreenSize.getWidth() * size * 0.75, configureScreenSize.getHeight() * size * 0.25);

        menuStage.setScene(menuScene);
        menuStage.show();
    }

    public void ChatScene() {
        chatStage = new Stage();
        chatStage.setTitle("Chat Window");

        chatStage.setWidth(configureScreenSize.getWidth() * 0.7 * 0.25);
        chatStage.setHeight(configureScreenSize.getHeight() * 0.70);

        chatStage.setX(configureScreenSize.getWidth() * 0.15 + configureScreenSize.getWidth() * 0.7 * 0.75);
        chatStage.setY(configureScreenSize.getHeight() * 0.15);

        chatStage.setResizable(false);
        chatStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
        });
        chatStage.initOwner(gameStage);

        //Pane used for the layout
        BorderPane layout = new BorderPane();

        //<editor-fold desc="Code for: Global/Team Chat + header">
        VBox chatHeader = new VBox();
        chatHeader.setPadding(new Insets(10, 10, 10, 10));

        Label headerText = new Label("Messages");
        headerText.setFont(new Font("Arial", 30));
        chatHeader.setAlignment(Pos.TOP_CENTER);

        chatBoxT1 = new VBox();
        chatBoxT2 = new VBox();
        chatBoxG = new VBox();
        chatTabs = new TabPane();

        chatPaneG = new ScrollPane();
        chatPaneG.setPadding(new Insets(10, 10, 10, 10));
        chatPaneG.setContent(chatBoxG);
        globalChat.setContent(chatPaneG);

        chatPaneT1 = new ScrollPane();
        chatPaneT1.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT1.setContent(chatBoxG);
        teamChat1.setContent(chatPaneT1);

        chatPaneT2 = new ScrollPane();
        chatPaneT2.setPadding(new Insets(10, 10, 10, 10));
        chatPaneT2.setContent(chatBoxG);
        teamChat2.setContent(chatPaneT2);

        //chatTabs.getTabs().addAll(globalChat,teamChat);
        chatTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        layout.setCenter(chatTabs);

        chatBoxT1.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneT1.setVvalue((Double)newValue );
            }
        });
        chatBoxT2.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneT2.setVvalue((Double)newValue );
            }
        });
        chatBoxG.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldvalue, Object newValue) {
                chatPaneG.setVvalue((Double) newValue);
            }
        });

        //</editor-fold>

        //<editor-fold desc="Code for: Sent Message Text-field/Button">
        //Make message send box
        VBox sendMessageBar = new VBox();
        sendMessageBar.setPadding(new Insets(10, 10, 10, 10));

        TextField messageBox = new TextField();
        messageBox.setPromptText("Write Message");
        messageBox.setPrefColumnCount(10);
        messageBox.getText();

        //Make send button
        Button sendButtons = new Button("Send");

        sendButtons.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //TODO output message from Space
                String message = messageBox.getText();
                System.out.println("message: " + message);
                try {
                    //String selectedTab = chatTabs.getSelectionModel().getSelectedItem().getText();
                    _clientSpace.put("UIToClient","chat", "Global," + message); //TODO replace id with current tap
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageBox.clear();
            }
        });

        sendMessageBar.getChildren().addAll(messageBox, sendButtons);

        layout.setBottom(sendMessageBar);

        //</editor-fold>

        Scene chatScene = new Scene(layout);
        chatStage.setScene(chatScene);
        chatStage.show();
    }

    public void AddChat(String id) {
        System.out.println("UiController adding chat to ui: " + id);
        Platform.runLater(() -> {
            if (id.equals("Team 1") && !chatTabs.getTabs().contains(teamChat1)) {
                chatTabs.getTabs().add(teamChat1);
            } else if (id.equals("Team 2") && !chatTabs.getTabs().contains(teamChat2)) {
                chatTabs.getTabs().add(teamChat2);
            } else if (id.equals("Global") && !chatTabs.getTabs().contains(globalChat)) {
                chatTabs.getTabs().add(globalChat);
            } else {
                System.out.println("No tab found with id: " + id);
            }
        });
    }

    public void RemoveChat(String id) {
        System.out.println("UiController removing chat from ui: " + id);
        Platform.runLater(() -> {
            if (id.equals("Team 1") && chatTabs.getTabs().contains(teamChat1)) {
                chatTabs.getTabs().remove(teamChat1);
            } else if (id.equals("Team 2") && chatTabs.getTabs().contains(teamChat2)) {
                chatTabs.getTabs().remove(teamChat2);
            } else if (id.equals("Global") && chatTabs.getTabs().contains(globalChat)) {
                chatTabs.getTabs().remove(globalChat);
            } else {
                System.out.println("No tab found with id: " + id);
            }
        });
    }

    public void UpdateChat(String name, String id, String message) {
        System.out.println("Updating ui!");
        switch (id) {
            case "Team 1" -> {
                Platform.runLater( () -> {
                    chatBoxT1.getChildren().add(new Label(name + " : " + message));
                });
            }
            case "Team 2" -> {
                Platform.runLater( () -> {
                    chatBoxT2.getChildren().add(new Label(name + " : " + message));
                });
            }
            case "Global" -> {
                Platform.runLater( () -> {
                    chatBoxG.getChildren().add(new Label(name + " : " + message));
                });
            }
        }
        try {
            _clientSpace.put("temp");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void voteBox(String name) {
        _playerName = name;
        votingWindow = new VotingUI();
            Platform.runLater( () -> {
                try {
                    gameStage.setScene(votingWindow.start());
                    setUpVoting();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }

    private void setUpVoting() {
        votingWindow.btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RadioButton selected =(RadioButton) votingWindow.tgGames.getSelectedToggle();
                System.out.println("Client chose: " + selected.getText());
                try {

                    _clientSpace.put("UIToClient", "tellGameMyVote", _playerName + "," + selected.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setUpReadyButton() {
        lobbyGame.readyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                lobbyGame.readyButton.setStyle("-fx-background-color: #00FF00;");
                lobbyGame.readyButton.setDisable(true);
                try {
                    _clientSpace.put("UIToClient", "YouAreReady", "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateGameScene(String gameScene, String playerAmount, String playerNumber) {
        _playerNumber = playerNumber;
        switch(_playerNumber){
            case "1" -> {
                team1L.setStyle("-fx-font-weight: bold;");
                team2L.setStyle("-fx-font-weight: regular;");
                team3L.setStyle("-fx-font-weight: regular;");
                team4L.setStyle("-fx-font-weight: regular;");
            }
            case "2" -> {
                team1L.setStyle("-fx-font-weight: regular;");
                team2L.setStyle("-fx-font-weight: bold;");
                team3L.setStyle("-fx-font-weight: regular;");
                team4L.setStyle("-fx-font-weight: regular;");
            }
            case "3" -> {
                team1L.setStyle("-fx-font-weight: regular;");
                team2L.setStyle("-fx-font-weight: regular;");
                team3L.setStyle("-fx-font-weight: bold;");
                team4L.setStyle("-fx-font-weight: regular;");
            }
            case "4" -> {
                team1L.setStyle("-fx-font-weight: regular;");
                team2L.setStyle("-fx-font-weight: regular;");
                team3L.setStyle("-fx-font-weight: regular;");
                team4L.setStyle("-fx-font-weight: bold;");
            }
        }
        _gameScene = gameScene;
        Platform.runLater( () -> {
            try {
                switch (_gameScene.toLowerCase()) {
                    case "lobby" -> {
                        lobbyGame = new LobbyUI();
                        gameStage.setScene(lobbyGame.start());
                        controls.setImage(null);
                        setUpReadyButton();
                    }
                    case "curvefever" -> {
                        curvefeverGame = new CurveFewer();
                        gameStage.setScene(curvefeverGame.start());
                        controls.setImage(curvefeverI);
                    }
                    case "minesweeper" -> {
                        Board board = (Board) _clientSpace.get(new ActualField("ClientToUI"),new ActualField("sendBoard"),new FormalField(Board.class))[2];
                        minesweeperGame = new MineSweeper(Integer.parseInt(playerAmount));
                        minesweeperScene = minesweeperGame.start(board);
                        gameStage.setScene(minesweeperScene);
                        controls.setImage(minesweeperI);
                        sendMinesweeperData();
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void updateGame(String data) {
        Platform.runLater( () -> {
            switch (_gameScene.toLowerCase()) {
                case "curvefever" -> {updateCurvefever(data);}
                case "minesweeper" -> {updateMinesweeper(data);}
            }
        });
        //System.out.println("UiController updatingGame!");
    }

    public void updateMinesweeper(String data) {

        boolean success = false;

        for (var entry: data.split(":")) {
            //playerNumber;m1;x;y
            String[] inputs = entry.split(";");
            //System.out.println("Player: " + inputs[0]);
            success = minesweeperGame.mouseAction(inputs[0]
                    , Integer.parseInt(inputs[1]) == 1
                    , Double.valueOf(inputs[2]).intValue()
                    , Double.valueOf(inputs[3]).intValue());
            if (success) {
                //System.out.println("New player in minesweeper!");
                break;
            }
        }

        //check if game is over

        //if not
        sendMinesweeperData();

    }

    public void sendMinesweeperData() {
        String dataOut = _playerNumber + ";" + minesweeperGame.mouseClicked + ";" + minesweeperGame.mouseX + ";" + minesweeperGame.mouseY;
        try {
            _clientSpace.put("UIToClient", "gameInput", dataOut);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        minesweeperGame.mouseClicked = "0";
        minesweeperGame.mouseX = -10;
        minesweeperGame.mouseY = -10;
    }

    public void updateCurvefever(String data) {

    }

    public void updateLobby(String name, String number, String readyStatus) {

        switch (number) {
            case "1" -> {Platform.runLater( () -> {
                lobbyGame.p1Name.setText("Player1: " + name);
                lobbyGame.display1Status.setText("Status: " + readyStatus);
            });}
            case "2" -> {Platform.runLater( () -> {
                lobbyGame.p2Name.setText("Player2: " + name);
                lobbyGame.display2Status.setText("Status: " + readyStatus);
            });}
            case "3" -> {Platform.runLater( () -> {
                lobbyGame.p3Name.setText("Player3: " + name);
                lobbyGame.display3Status.setText("Status: " + readyStatus);
            });}
            case "4" -> {Platform.runLater( () -> {
                lobbyGame.p4Name.setText("Player4: " + name);
                lobbyGame.display4Status.setText("Status: " + readyStatus);
            });}
        }

    }

    private void updateCurvefever() {

    }

    private void updateMinesweeper() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}

class ClientReceiver implements Runnable {
    private UIController _uiController;
    private SequentialSpace _fromClientSpace;

    public ClientReceiver(UIController client, SequentialSpace fromClientSpace) {
        _uiController = client;
        _fromClientSpace = fromClientSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromClientSpace.get(new ActualField("ClientToUI"), new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "AddChat" -> _uiController.AddChat(data[0]);
                    case "RemoveChat" -> _uiController.RemoveChat(data[0]);
                    case "UpdateChat" -> _uiController.UpdateChat(data[0], data[1], data[2]);
                    case "UpdateLobby" -> _uiController.updateLobby(data[0], data[1], data[3]);
                    case "votingTime" -> _uiController.voteBox(data[0]);
                    case "newGame" -> _uiController.updateGameScene(data[0], data[1], data[2]);
                    case "UpdateGame" -> _uiController.updateGame(data[0]);
                }
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}
