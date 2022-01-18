package TapasGames.Client;

import JspaceFiles.jspace.*;
import TapasGames.UI.UIController;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

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
            _serverSpace = new RemoteSpace(serverIpWithPort + "clientServer?keep");
            System.out.println("Writing to serverSpace: " + _serverSpace.toString());
            new Thread(new UIReceiver(this, uiSpace)).start();
            new Thread(new ChatReceiver(this, new RemoteSpace(_serverIpWithPort + "ChatToClient:" + _name + "?keep"))).start();
            new Thread(new ServerReceiver(this, _serverSpace)).start();
            _serverSpace.put("ClientBackToServer", name, "ClientCreated");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return _name;
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

    public void sendKeyboardInputToGame() {

    }

    public void sendMouseInputToGame() {

    }

    public void updateChatUI(String name, String id, String message) {
        System.out.println("in " + id + ": " + name + " Says: " + message);
        try {
            _uiSpace.put("ClientToUI", "UpdateChat", name + "," + id + "," + message);
        } catch (Exception ignored) {

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
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
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
                Object[] tuple = _fromInputSpace.get(new ActualField("UIToClient"),
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
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