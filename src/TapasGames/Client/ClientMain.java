package TapasGames.Client;

import JspaceFiles.jspace.*;
import TapasGames.UI.UIController;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientMain {
    private UIController _ui;
    private String _name;
    private String _serverIpWithPort;
    private RemoteSpace _serverSpace;
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
            _serverSpace = new RemoteSpace(serverIpWithPort + "clientServer?keep");
            System.out.println("Writing to serverSpace: " + _serverSpace.toString());
            new Thread(new UIReceiver(this, uiSpace)).start();
            new Thread(new ChatReceiver(this, new RemoteSpace(_serverIpWithPort + "ChatToClient:" + _name + "?keep"))).start();
            new Thread(new ServerReceiver(this, _serverSpace)).start();
            new Thread(new GameReceiver(this, new RemoteSpace(_serverIpWithPort + "toGameRoom:?keep"))).start();
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

    public void addChatRoom(String id) {
        try {
            _uiSpace.put("ClientToUI", "AddChat", "" + id);
            _serverSpace.put("ClientBackToServer", _name, "ChatRoomAdded");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendDataToChatRoom(String id, String data) {
        try { //id = tap
            System.out.println("Client received: " + id + " : " + data);
            new RemoteSpace(_serverIpWithPort + "toChatRoom:" + id + "?keep")
                    .put("sendMessage", _name + "," + data);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToGameRoom(String data) {
        System.out.println("Client received data it should send to game: " + data);
        try {
            new RemoteSpace(_serverIpWithPort + "toGameRoom:" + "?keep")
                    .put("ClientToGameRoom",_name,data);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendVote(String name, String message) {
        System.out.println("Client received data it should send to game: " + message);
        try {
            new RemoteSpace(_serverIpWithPort + "toVoting:" + "?keep")
                    .put("votingResult", name + "," + message);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateChatUI(String name, String id, String message) {
        System.out.println("in " + id + ": " + name + " Says: " + message);
        try {
            _uiSpace.put("ClientToUI", "UpdateChat", name + "," + id + "," + message);
        } catch (Exception ignored) {

        }
    }

    public void updateGame(String data){ // data:    name;left;right;up;down;m1;x;y : name;left;right;up;down;m1;x;y
        System.out.println(_name + "has gotten the game data and is sending it to UI");
        //TODO send data to UI/Client side game
    }

    public void updateLobby(String name, String number, String readyStatus) {
        if (_name.equals(name)) _playerNumber = number;
        try {
            _uiSpace.put("ClientToUI", "UpdateLobby", name + "," + number + "," + "messagePlaceholder" + "," + readyStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlayers(String name, String number) {
        System.out.println("Client udating: " + name + " he/she is: " + number);
        try {
            _uiSpace.put("ClientToUI", "UpdatePlayers", name + "," + number); //TODO PROTOCOL
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendImReady() {
        System.out.println(_name + " telling server im ready!");
        try {
            _serverSpace.put("ClientToServer", "clientIsReady", _name); //TODO PROTOCOL
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void votingTime() {
        System.out.println("Im about to make my vote! #trumpsupporter");//hmmm
        try {
            _uiSpace.put("ClientToUI", "votingTime", "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newGame(String newGame) {
        try {
            _uiSpace.put("ClientToUI", "newGame", newGame);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void rockTheVote() {
        try {
            new RemoteSpace(_serverIpWithPort + "toVoting:" + "?keep")
                    .put("rockTheVote", _name);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
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
                }
            } catch (InterruptedException ignored) {
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
                }
            } catch (Exception ignored) {
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
                System.out.println("I recieved something in my receiver from game.");
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "votingTime" -> _client.votingTime();
                    case "newGame" -> _client.newGame(data[0]);
                    case "updateGame" -> _client.updateGame(data[0]);
                }
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}