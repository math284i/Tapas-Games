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
        _repository.addGate(_ipWithPort + "?keep");
        _clients = new ArrayList<>();
        _chatController = new ChatController(_repository, _chatSpace);

        new Thread(new ClientReceiver(this,_clientSpace)).start();
    }

    public void addClient(String name){
        //TODO check if valid
        _clients.add(name);
    }

    private void createChatRoom(String id) {
        try {
            _chatSpace.put("fromServer","addChatRoom",id);
            _chatSpace.get(new ActualField ("fromChat"), new ActualField("chatRoomAdded"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClientToChatRoom(String name, String id) {
        try {
            _chatSpace.put("fromServer","addClient",name+","+id);
            _clientSpace.put(name,"",id); //TODO UPDATE
            _chatSpace.get(new ActualField ("fromChatRoom"),
                    new ActualField("clientAdded"), new FormalField(String.class));
            _clientSpace.get(new ActualField ("from" + name),
                    new ActualField("chatRoomAdded"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeClientFromChatRoom(String name, String id) {
        try {
            _repository.get("toChatRoom: " + id).put("removeClient",name,"");
            _clientSpace.put(name,"",id); //TODO UPDATE
            _chatSpace.get(new ActualField ("fromChatRoom"),
                    new ActualField("clientRemoved"), new FormalField(String.class));
            _clientSpace.get(new ActualField ("from" + name),
                    new ActualField("chatRoomRemoved"));
        } catch (Exception e) {
            e.printStackTrace();
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
                Object[] tuple = _fromClientSpace.get(
                        new ActualField("toServer"), new FormalField(String.class)
                        , new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "addClient" -> _server.addClient(tuple[1].toString());
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}