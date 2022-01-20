package TapasGames.Client;

import JspaceFiles.jspace.*;
import TapasGames.Game.MiniGames.Board;
import TapasGames.UI.UIController;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

public class ClientMain {
    private UIController _ui;
    private String _name;
    private String _serverIpWithPort;
    private RemoteSpace _serverSpace;
    private RemoteSpace _gameSpace;
    private RemoteSpace _voteSpace;
    private HashMap<String, RemoteSpace> _chatSpaces;
    private SequentialSpace _uiSpace;
    private String _playerNumber = "0";

    //tcp://localhost:31415/
    public ClientMain(UIController ui, String name, String serverIpWithPort, SequentialSpace uiSpace) throws IOException {
        _ui = ui;
        _name = name;
        _serverIpWithPort = serverIpWithPort;
        _uiSpace = uiSpace;

        try {
            _ui.start(new Stage(), _uiSpace);
            _serverSpace = new RemoteSpace(_serverIpWithPort + "clientServer?keep");
            _gameSpace = new RemoteSpace(_serverIpWithPort + "toGameRoom:?keep");
            _voteSpace = new RemoteSpace(_serverIpWithPort + "toVoting:?keep");
            _chatSpaces = new HashMap<>();
            System.out.println("Writing to serverSpace: " + _serverSpace.toString());
            new Thread(new UIReceiver(this, uiSpace)).start();
            new Thread(new ChatReceiver(this, new RemoteSpace(_serverIpWithPort + "ChatToClient:" + _name + "?keep"))).start();
            new Thread(new ServerReceiver(this, _serverSpace)).start();
            new Thread(new GameReceiver(this, _gameSpace)).start();
            _serverSpace.put("ClientBackToServer", name, "ClientCreated");
            System.out.println("Client wrote to serverSpace its been Created!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return _name;
    }

    public String getPlayerNumber() {
        return _playerNumber;
    }

    public void addChatRoom(String id) throws InterruptedException, IOException {
            _chatSpaces.put(id, new RemoteSpace(_serverIpWithPort + "toChatRoom:" + id + "?keep"));
            _uiSpace.put("ClientToUI", "AddChat", "" + id);
            _serverSpace.put("ClientBackToServer", _name, "ChatRoomAdded");
    }

    public void removeChatRoom(String id) throws InterruptedException, IOException {
        _chatSpaces.remove(id);
        _uiSpace.put("ClientToUI", "RemoveChat", "" + id);
        _serverSpace.put("ClientBackToServer", _name, "ChatRoomRemoved");
    }

    public void sendDataToChatRoom(String id, String data) throws InterruptedException {
            System.out.println("Client received: " + id + " : " + data);
            _chatSpaces.get(id).put("sendMessage", _name + "," + data);
    }

    public void sendDataToGameRoom(String data) throws InterruptedException {
            _gameSpace.put("ClientToGameRoom", _name, data);
    }

    public void sendVote(String name, String message) throws InterruptedException {
            _voteSpace.put("votingResult", name + "," + message);
    }

    public void updateChatUI(String name, String id, String message) throws InterruptedException {
        System.out.println("in " + id + ": " + name + " Says: " + message);
        _uiSpace.put("ClientToUI", "UpdateChat", name + "," + id + "," + message);
    }

    public void updateGame(String data) throws InterruptedException { // data:    playerNumber;left;right;up;down;m1;x;y : playerNumber;left;right;up;down;m1;x;y
        //System.out.println(_name + "has gotten the game data and is sending it to UI: " + data);
        _uiSpace.put("ClientToUI", "UpdateGame", data);
    }

    public void updateLobby(String name, String number, String readyStatus) throws InterruptedException {
        if (_name.equals(name)) _playerNumber = number;
            _uiSpace.put("ClientToUI", "UpdateLobby", name + "," + number + "," + "messagePlaceholder" + "," + readyStatus);
    }

    public void updatePlayers(String name, String number) throws InterruptedException {
        System.out.println("Client udating: " + name + " he/she is: " + number);
            _uiSpace.put("ClientToUI", "UpdatePlayers", name + "," + number);
    }

    public void sendImReady() throws InterruptedException {
        System.out.println(_name + " telling server im ready!");
            _serverSpace.put("ClientToServer", "clientIsReady", _name);
    }

    public void votingTime(String score) throws InterruptedException {
        System.out.println("Im about to make my vote! #trumpsupporter");//hmmm
            _uiSpace.put("ClientToUI", "votingTime", score);
    }

    public void newGame(String newGame, String playerAmount) throws InterruptedException {
            if (newGame.equals("Minesweeper")) {
                Board board = (Board) _gameSpace.get(new ActualField("GameRoomToClient"), new ActualField(_name), new ActualField("sendBoard"), new FormalField(Board.class))[3];
                _uiSpace.put("ClientToUI", "sendBoard", board);
            }
            _uiSpace.put("ClientToUI", "newGame", newGame + "," + playerAmount + "," + _playerNumber);
    }

    public void rockTheVote() throws InterruptedException {
        _voteSpace.put("rockTheVote", _name);
    }

    public void tellServerGameOver(String playersWon) throws InterruptedException {
        _serverSpace.put("ClientToServer", "gameOver", playersWon);
    }

    public void tellServerImLeaving() throws InterruptedException {
        _serverSpace.put("ClientToServer", "clientLeaving", _name);
    }
}

class ServerReceiver implements Runnable {
    private ClientMain _client;
    private RemoteSpace _fromServerSpace;

    public ServerReceiver(ClientMain client, RemoteSpace fromServerSpace) {
        _client = client;
        _fromServerSpace = fromServerSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromServerSpace.get(new ActualField("ServerToClient"),
                        new ActualField(_client.getName()), new FormalField(String.class), new FormalField(String.class));
                System.out.println("Client recieved something from server!");
                String[] data = tuple[3].toString().split(","); //name, playerNumber, readyStatus
                switch (tuple[2].toString()) {
                    case "addChatRoom" -> _client.addChatRoom(data[0]);
                    case "updateLobby" -> _client.updateLobby(data[0], data[1], data[2]);
                    case "updatePlayers" -> _client.updatePlayers(data[0], data[1]);
                    case "removeChatRoom" -> _client.removeChatRoom(data[0]);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class UIReceiver implements Runnable {
    private ClientMain _client;
    private SequentialSpace _fromInputSpace;

    public UIReceiver(ClientMain client, SequentialSpace fromInputSpace) {
        _client = client;
        _fromInputSpace = fromInputSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromInputSpace.get(new ActualField("UIToClient"),
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "chat" -> _client.sendDataToChatRoom(data[0], data[1]);
                    case "YouAreReady" -> _client.sendImReady();
                    case "tellGameMyVote" -> _client.sendVote(data[0], data[1]);
                    case "rockTheVote" -> _client.rockTheVote();
                    case "gameInput" -> _client.sendDataToGameRoom(data[0]);
                    case "gameOver" -> _client.tellServerGameOver(data[0]);
                    case "leaving" -> _client.tellServerImLeaving();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ChatReceiver implements Runnable {
    private ClientMain _client;
    private RemoteSpace _fromChatSpace;

    public ChatReceiver(ClientMain client, RemoteSpace fromChatSpace) {
        _client = client;
        _fromChatSpace = fromChatSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromChatSpace.get(new FormalField(String.class));
                System.out.println("I recieved something in my receiver from chat.");
                String[] data = tuple[0].toString().split(",");
                _client.updateChatUI(data[0], data[1], data[2]);
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}

class GameReceiver implements Runnable {
    private ClientMain _client;
    private RemoteSpace _fromGameSpace;

    public GameReceiver(ClientMain client, RemoteSpace fromGameSpace) {
        _client = client;
        _fromGameSpace = fromGameSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromGameSpace.get(new ActualField("GameRoomToClient")
                        , new ActualField(_client.getName()), new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "votingTime" -> _client.votingTime(data[0]);
                    case "newGame" -> _client.newGame(data[0], data[1]);
                    case "updateGame" -> _client.updateGame(data[0]);
                }
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}