package TapasGames.Client;

import JspaceFiles.jspace.*;
import TapasGames.UI.UIController;
import javafx.stage.Stage;

import java.io.IOException;

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
            _ui.start(new Stage());
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
            System.out.println("Client recieved: " + id + " : " + data);
            new RemoteSpace(_serverIpWithPort + "toChatRoom:" + id + "?keep")
                    .put("sendMessage", _name + "," + data);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToGameRoom(String data) {
        System.out.println("Client recieved: " + data);
        try {
            new RemoteSpace(_serverIpWithPort + "toGameRoom:" + "?keep").put("temp");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendKeyboardInputToGame() {

    }

    public void sendMouseInputToGame() {

    }

    public void updateChatUI(String name, String id, String message) {
        System.out.println("in " + id + ": " + name + " Says: " + message);
        try {
            _uiSpace.put("ClientToUI", "UpdateChat", name + "," + id + "," + message); //TODO PROTOCOL
        } catch (Exception ignored) {

        }
    }

    public void updateLobby(String name, String number) {
        if (_name.equals(name)) _playerNumber = number;
        try {
            _uiSpace.put("ClientToUI", "UpdateLobby", name + "," + number); //TODO PROTOCOL
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
            _serverSpace.put("ClientToServer", _name, "clientIsReady", "" + _name); //TODO PROTOCOL - Also should name be here? (the first)
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

    public void tellGameMyVote() {

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
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "addChatRoom" -> _client.addChatRoom(data[0]);
                    case "updateLobby" -> _client.updateLobby(data[0], data[1]);
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
                    case "keyboardInput" -> _client.sendKeyboardInputToGame();
                    case "mouseInput" -> _client.sendMouseInputToGame();
                    case "YouAreReady" -> _client.sendImReady();
                    case "tellGameMyVote" -> _client.tellGameMyVote();
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
                }
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}