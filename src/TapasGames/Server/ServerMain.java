package TapasGames.Server;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;
import TapasGames.Chat.ChatController;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ServerMain {
    private ServerUI _ui;
    private String _ip;
    private String _port;
    private String _ipWithPort;
    private ArrayList<String> _clients;
    private SequentialSpace _clientSpace;
    private SequentialSpace _chatSpace;
    private SequentialSpace _gameSpace;
    private SpaceRepository _repository;
    private ChatController _chatController;

    public ServerMain(ServerUI ui, String ip, String port) throws Exception {
        _ui = ui;
        _ip = ip;
        _port = port;
        _ipWithPort = ip + port + "/";
        _repository = new SpaceRepository();
        _clientSpace = new SequentialSpace();
        _chatSpace = new SequentialSpace();
        _repository.add("clientServer", _clientSpace);
        _repository.addGate(_ipWithPort + "?keep");
        _clients = new ArrayList<>();
        _chatController = new ChatController(_repository, _chatSpace);

        createChatRoom("Global");
        _ui.start(new Stage());
        new Thread(new ClientReceiver(this,_clientSpace)).start();
    }

    public void addClient(String name){
        //TODO check if valid
        System.out.println("Server adding: " + name);
        _clients.add(name);
        try {
            SequentialSpace space = new SequentialSpace();
            _repository.add("ChatToClient:" + name, space);
            _clientSpace.get(new ActualField(name), new ActualField("ClientCreated"));
            addClientToChatRoom(name, "Global");
            //TODO check if client is added to chat room
            //TODO write to client it has been added to chat room
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createChatRoom(String id) {
        try {
            _chatSpace.put("fromServer","addChatRoom",id);
            System.out.println("ServerMain asked chatRoom for: " + id);
            _chatSpace.get(new ActualField ("fromChat"), new ActualField("chatRoomAdded"));
            System.out.println("Server recieved chatRoomAdded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClientToChatRoom(String name, String id) {
        try {
            _chatSpace.put("fromServer","addClient",name+","+id);
            _clientSpace.put(name,"addChatRoom",id); //TODO UPDATE
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
            _repository.get("toChatRoom:" + id).put("removeClient",name,"");
            _clientSpace.put(name,"",id); //TODO UPDATE
            _chatSpace.get(new ActualField ("fromChatRoom"),
                    new ActualField("clientRemoved"), new FormalField(String.class));
            _clientSpace.get(new ActualField ("from" + name),
                    new ActualField("chatRoomRemoved"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                System.out.println("Someone wrote to serverSpace!");
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "addClient" -> _server.addClient(tuple[1].toString());
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}