package TapasGames.Server;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;
import TapasGames.Chat.ChatController;
import TapasGames.UI.UIController;

import java.util.ArrayList;

public class ServerMain {
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

        new Thread(new ClientReceiver(this,_clientSpace)).start();
    }

    public void addClient(String name){
        //TODO check if valid
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
    //I need a MainController
    //Tell MainController im ready to receive connections!

}
class ClientReceiver implements Runnable{
    ServerMain _server;
    SequentialSpace _fromClientSpace;

    public ClientReceiver(ServerMain server, SequentialSpace fromClientSpace){
        _server = server;
        _fromClientSpace = fromClientSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] data = _fromClientSpace.get(
                        new ActualField("toServer"), new FormalField(String.class)
                        , new FormalField(String.class), new FormalField(String.class));
                String[] tuple = data[3].toString().split(",");
                switch (data[2].toString()) {
                    case "addClient" -> _server.addClient(data[1].toString());
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}