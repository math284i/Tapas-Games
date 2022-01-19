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
    //tcp://localhost:31415/
    public ClientMain(UIController ui, String name, String serverIpWithPort, SequentialSpace uiSpace) throws IOException {
        _ui = ui;
        _name = name;
        _serverIpWithPort = serverIpWithPort;
        _uiSpace = uiSpace;

        try {
            _ui.start(new Stage());
            _serverSpace = new RemoteSpace(serverIpWithPort + "clientServer" + "?keep");
            //_serverSpace = new RemoteSpace(serverIpWithPort + "clientServer");
            System.out.println("Writing to serverspace: " + _serverSpace.toString());
            _serverSpace.put("toServer", name, "addClient", "PlaceHolder,PlaceHolder");
            //_serverSpace.get(new ActualField(name), new ActualField("Test"));

            System.out.println("Have wrote to server!");
            //TODO get from server, that we can continue
            new Thread(new UIReceiver(this, uiSpace)).start();
            new Thread(new ChatReceiver(this, new RemoteSpace(_serverIpWithPort + "ChatToClient:" + _name + "?keep"))).start();
            new Thread(new ServerReceiver(this, _serverSpace)).start();
            _serverSpace.put(name, "ClientCreated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return _name;
    }

    public void addChatRoom(String id) {
        try {
            _uiSpace.put("AddChat", "placeholder" + "," + id + "," + "placeholder");
            //TODO get confirmation from chatRoom
            //TODO Send to _chatSpace(new ActualField("from" + name), new ActualField("chatRoomAdded));
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

    public void sendKeyboardInputToGame() {

    }

    public void sendMouseInputToGame() {

    }

    public void updateChatUI(String name, String id, String message) {
        System.out.println("in "+id+": "+name + " Says: " + message);
        try {
            _uiSpace.put("UpdateChat", name + "," + id + "," + message);
        } catch(Exception ignored) {

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
                Object[] tuple = _fromServerSpace.get(
                        new ActualField(_client.getName()), new FormalField(String.class), new FormalField(String.class));
                System.out.println("Client recieved something from server!");
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addChatRoom" -> _client.addChatRoom(data[0]);
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
                Object[] tuple = _fromInputSpace.get(
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[1].toString().split(",");
                switch (tuple[0].toString()) {
                    case "chat" -> _client.sendDataToChatRoom(data[0], data[1]);
                    case "keyboardInput" -> _client.sendKeyboardInputToGame();
                    case "mouseInput" -> _client.sendMouseInputToGame();
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
                Object[] tuple = _fromGameSpace.get(new ActualField(_client.getName()), new FormalField(String.class));
                System.out.println("I recieved something in my receiver from game.");
                String[] data = tuple[1].toString().split(",");
                _client.updateChatUI(data[0], data[1], data[2]);
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}