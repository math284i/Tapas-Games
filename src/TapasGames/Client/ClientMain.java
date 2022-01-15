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
            _serverSpace.put("toServer",name,"addClient","");
        } catch (Exception ignored) {
        }


        new Thread(new UIReceiver(this, uiSpace)).start();
    }

    public String getName() {
        return _name;
    }

    public void addChatRoom(String id) {
        try {
            new Thread(new ChatReceiver(this, new RemoteSpace(_serverIpWithPort + "ChatToClient: " + id + _name))).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendDataToChatRoom(String id, String data) {
        try {
            System.out.println("Client recieved: " + id + " : " + data);
            new RemoteSpace(_serverIpWithPort + "toChatRoom:" + id + "?keep").put("sendMessage", _name, data);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendKeyboardInputToGame() {

    }

    public void sendMouseInputToGame() {

    }

    public void updateChatUI(String name, String message, String id) {
        System.out.println("in "+id+": "+name + " Says: " + message);
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
                Object[] data = _fromServerSpace.get(
                        new ActualField(_client.getName()), new FormalField(String.class), new FormalField(String.class));
                String[] tuple = data[2].toString().split(",");
                switch (data[1].toString()) {
                    case "addChatRoom" -> _client.addChatRoom(tuple[0]);
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
            } catch (InterruptedException ignored) {
            }
        }
    }
}

/*
class ChatSender implements Runnable {
    private ClientMain _client;
    private String _senderIp;
    private RemoteSpace _space;
    Scanner cSource = new Scanner(System.in);

    public ChatSender(ClientMain client, String senderIp) {
        _client = client;
        _senderIp = senderIp;
        try {
            _space = new RemoteSpace(_senderIp);
        } catch (Exception e) {
            System.out.println("Failed while trying to connect to IP: " + _senderIp + "\n with: " + e);
        }
    }

    @Override
    public void run() {
        String message = "";
        String type = "chat";
        while (true) {
            message = cSource.nextLine();
            try {
                _space.put(_client.getName(), message, type);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
*/
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
                String[] data = tuple[0].toString().split(",");
                _client.updateChatUI(data[0], data[1], data[2]);
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}