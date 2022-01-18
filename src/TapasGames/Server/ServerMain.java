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
    private SequentialSpace _startUpSpace;
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
        _repository.add("startUpServer",_startUpSpace);
        _repository.addGate(_ipWithPort + "?keep");
        _clients = new ArrayList<>();
        _chatController = new ChatController(_repository, _chatSpace);

        createChatRoom("Global");
        _ui.start(new Stage());
        new Thread(new ClientReceiver(this, _clientSpace)).start();
        new Thread(new StartUpReceiver(this, _startUpSpace)).start();
    }

    public void addClient(String name) {
        try {
            if (_clients.contains(name)) {
                System.out.println("Server not adding: " + name);
                _startUpSpace.put("ServerBackToStartUp",_ipWithPort,false);
            } else {
                System.out.println("Server adding: " + name);
                _clients.add(name);
                SequentialSpace space = new SequentialSpace();
                _repository.add("ChatToClient:" + name, space);
                _startUpSpace.put("ServerBackToStartUp",_ipWithPort, true);
                _clientSpace.get(new ActualField("ClientBackToServer"), new ActualField(name), new ActualField("ClientCreated"));
                System.out.println("ServerMain received clientCreated!");
                addClientToChatRoom(name, "Global");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String name){

    }

    private void createChatRoom(String id) {
        try {
            _chatSpace.put("ServerToChat", "addChatRoom", id);
            System.out.println("ServerMain asked chat controller to create chat room: " + id);
            _chatSpace.get(new ActualField("ChatBackToServer"), new ActualField("ChatRoomAdded"));
            System.out.println("Server received chatRoomAdded!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClientToChatRoom(String name, String id) {
        try {
            System.out.println("Server Adding: " + name + " to: " + id);
            _chatSpace.put("ServeToChat", "addClient", name + "," + id);
            _clientSpace.put("ServerToClient",name, "addChatRoom", id);
            _chatSpace.get(new ActualField("ChatBackToServer"), //was fromChatRoom
                    new ActualField("ClientAdded")); //Used to have small c, Used to have an extra field for a string.
            System.out.println("Client is added from serverMain");
            _clientSpace.get(new ActualField ("ClientBackToServer"), new ActualField(name),
                    new ActualField("ChatRoomAdded"));
            System.out.println("Everything is done in Server AddClientToChatRoom");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeClientFromChatRoom(String name, String id) {
        try {
            System.out.println("Server Removing: " + name + " from: " + id);
            _chatSpace.put("ServeToChat", "removeClient", name + "," + id);
            _clientSpace.put("ServerToClient",name, "removeChatRoom", id);
            _chatSpace.get(new ActualField("ChatBackToServer"), //was fromChatRoom
                    new ActualField("ClientRemoved")); //Used to have small c, Used to have an extra field for a string.
            System.out.println("Client is removed from serverMain");
            _clientSpace.get(new ActualField ("ClientBackToServer"), new ActualField(name),
                    new ActualField("ChatRoomRemoved"));
            System.out.println("Everything is done in Server RemoveClientToChatRoom");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ClientReceiver implements Runnable {
    ServerMain _server;
    SequentialSpace _fromClientSpace;

    public ClientReceiver(ServerMain server, SequentialSpace fromClientSpace) {
        _server = server;
        _fromClientSpace = fromClientSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromClientSpace.get(
                        new ActualField("ClientToServer"), new FormalField(String.class)
                        , new FormalField(String.class), new FormalField(String.class));
                System.out.println("Someone wrote to serverSpace!, my space is: " + _fromClientSpace.toString());
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "removeClient" -> {
                        //System.out.println("Removing client!");
                        //_server.removeClient(tuple[1].toString());
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}

class StartUpReceiver implements Runnable {
    ServerMain _server;
    SequentialSpace _fromStartUpSpace;

    public StartUpReceiver(ServerMain server, SequentialSpace fromStartUpSpace) {
        _server = server;
        _fromStartUpSpace = fromStartUpSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromStartUpSpace.get(
                        new ActualField("StartUpToServer"), new FormalField(String.class)
                        , new FormalField(String.class));
                System.out.println("Startup wrote to serverSpace!, my space is: " + _fromStartUpSpace.toString());
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addClient" -> {
                        _server.addClient(data[0]);
                    }
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}