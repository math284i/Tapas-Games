package TapasGames.Client;

import JspaceFiles.jspace.*;
import TapasGames.UI.UIController;

public class ClientMain implements Runnable{
    private UIController _ui;
    private String _name;
    private SequentialSpace _serverSpace;
    private SequentialSpace _uiSpace;
    private SpaceRepository _repository;

    public ClientMain(String name, SpaceRepository repository, SequentialSpace serverSpace) {
        _name = name;
        _serverSpace = serverSpace;
        _repository = repository;

        new Thread(new ChatReceiver(this,_repository.get("ChatToClient: "+_name))).start();
    }

    public String getName() {
        return _name;
    }

    private void sendDataToChatRoom(int id, String data){
        try{
            _repository.get("toChatRoom: "+id).put(0,_name,data);
        } catch (InterruptedException ignored) {}
    }

    private void sendKeyboardInputToGame() {

    }

    private void sendMouseInputToGame() {

    }

    public void updateChatUI(String name, String message) {
        System.out.println(name + " Says: " + message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] data = _uiSpace.get(
                        new ActualField(_name),
                        new FormalField(String.class), new FormalField(String.class));
                switch (data[1].toString()) {
                    case "chat" -> sendDataToChatRoom(); //Tuple = (id, message); //TODO
                    case "keyboardInput" -> sendKeyboardInputToGame();
                    case "mouseInput" -> sendMouseInputToGame();
                }
            } catch (InterruptedException ignored) {}
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
    private Space _fromChatSpace;

    public ChatReceiver(ClientMain client, Space fromChatSpace) {
        _client = client;
        _fromChatSpace = fromChatSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] data = _fromChatSpace.get(new FormalField(String.class), new FormalField(String.class));
                _client.updateChatUI(data[0].toString(), data[1].toString());
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}