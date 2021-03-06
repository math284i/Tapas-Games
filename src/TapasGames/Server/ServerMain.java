package TapasGames.Server;

import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;
import TapasGames.Chat.ChatController;
import TapasGames.Client.ClientMain;
import TapasGames.UI.UIController;

import java.io.IOException;
import java.util.ArrayList;

public class ServerMain implements Runnable{
    private UIController _ui;
    private String _ip;
    private String _port;
    private String _ipWithPort;
    private ArrayList<String> _clients;
    private SequentialSpace _clientSpace;
    private SequentialSpace _chatSpace;
    private SequentialSpace _gameSpace;
    private SpaceRepository _repository;
    private ChatController _chatController;

    public ServerMain(UIController ui, String ip, String port) {
        _ui = ui;
        _ip = ip;
        _port = port;
        _ipWithPort = "tcp://localhost:" + port + "/";
        _repository = new SpaceRepository();
        _clientSpace = new SequentialSpace();
        _chatSpace = new SequentialSpace();
        _repository.add("clientServer", _clientSpace);
        _repository.add("chatServer", _chatSpace);
        _repository.add("gameServer", _gameSpace);
        _repository.addGate(_ipWithPort + "?keep");
        _clients = new ArrayList<>();
        _chatController = new ChatController(_repository);
    }

    public void addClient(String name){
        _clients.add(name);
    }

    private void createChatRoom(int id) {
        _chatController.addChatRoom(id);
    }

    public void addClientToChatRoom(String name, int id) {
        try {
            _repository.get("toChatRoom: " + id).put("addClient",name,"");
            _clientSpace.put(name,"",id); //TODO UPDATE

        } catch (Exception e) {
            System.out.println("Failed putting into toChatRoom: " + id + "\n with: " + e);
        }
    }

    public void removeClientFromChatRoom(String name, int id) {
        try {
            _repository.get("toChatRoom: " + id).put("removeClient",name,"");
            _clientSpace.put(name,"",id); //TODO UPDATE

        } catch (Exception e) {
            System.out.println("Failed putting into toChatRoom: " + id + "\n with: " + e);
        }
    }

    @Override
    public void run() {
        while (true) {
            /*
            try {
                Object[] data = _fromChatSpace.get(new FormalField(String.class), new FormalField(String.class));
                _client.updateChatUI(data[0].toString(), data[1].toString());
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
            */
        }
    }

    //I need a MainController
    //Tell MainController im ready to receive connections!

}
