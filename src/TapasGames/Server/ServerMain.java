package TapasGames.Server;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;
import TapasGames.Chat.ChatController;
import TapasGames.Game.GamesController;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerMain {
    private ServerUI _ui;
    private String _ip;
    private String _port;
    private String _ipWithPort;
    private ArrayList<String> _clients;
    private HashMap<String, String> _clientsAndNumbers;
    private ArrayList<String> _readyClients;
    private SequentialSpace _clientSpace;
    private SequentialSpace _chatSpace;
    private SequentialSpace _gameSpace;
    private SequentialSpace _startUpSpace;
    private SpaceRepository _repository;
    private ChatController _chatController;
    private GamesController _gamesController;

    public ServerMain(ServerUI ui, String ip, String port) throws Exception {
        _ui = ui;
        _ip = ip;
        _port = port;
        _ipWithPort = ip + port + "/";
        _repository = new SpaceRepository();
        _clientSpace = new SequentialSpace();
        _chatSpace = new SequentialSpace();
        _gameSpace = new SequentialSpace();
        _startUpSpace = new SequentialSpace();
        _repository.add("clientServer", _clientSpace);
        _repository.add("startUpServer",_startUpSpace);
        _repository.addGate(_ipWithPort + "?keep");
        _clients = new ArrayList<>();
        _readyClients = new ArrayList<>();
        _clientsAndNumbers = new HashMap<>();
        _chatController = new ChatController(_repository, _chatSpace);
        _gamesController = new GamesController(_repository, _gameSpace);

        createChatRoom("Global");
        _ui.start(new Stage());
        new Thread(new ClientReceiver(this, _clientSpace)).start();
        new Thread(new StartUpReceiver(this, _startUpSpace)).start();
        new Thread(new GameReceiver(this, _gameSpace)).start();
    }

    public void addClient(String name) {
        try {
            if (_clients.contains(name)) {
                System.out.println("Server not adding: " + name);
                _startUpSpace.put("ServerBackToStartUp",_ipWithPort,"false");
            } else {
                System.out.println("Server adding: " + name);
                _clients.add(name);
                SequentialSpace space = new SequentialSpace();
                _repository.add("ChatToClient:" + name, space);
                _startUpSpace.put("ServerBackToStartUp",_ipWithPort, "true");
                _clientSpace.get(new ActualField("ClientBackToServer"), new ActualField(name), new ActualField("ClientCreated"));
                System.out.println("ServerMain received clientCreated!");
                addClientToChatRoom(name, "Global");
                addClientToGame(name);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addClientToGame(String name) {
        try {
            _gameSpace.put("ServerToGame", "addNewPlayer", name);
            Object[] tuple = _gameSpace.get(new ActualField("GameBackToServer")
                    , new ActualField(name + " has been added"), new FormalField(String.class));
            System.out.println(name + " has been added to game as number: " + tuple[2].toString());

            //TODO send to all clients, name has been added as player number 1.
            for (String client: _clients) {
                _clientSpace.put("ServerToClient", client, "updateLobby", "" + name + "," + tuple[2].toString());
            }

            for (var entry : _clientsAndNumbers.entrySet()) {
                _clientSpace.put("ServerToClient", name, "updateLobby", "" + entry.getKey() + "," + entry.getValue());
            }
            _clientsAndNumbers.put(name, tuple[2].toString());

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
            _chatSpace.put("ServerToChat", "addClient", name + "," + id);
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

    public void clientIsReady(String name) {
        _readyClients.add(name);

        if (_readyClients.size() == _clients.size()) {
            System.out.println("All players are ready to RUUUUUMBLE!");
            try {
                _gameSpace.put("ServerToGame", "votingTime", "");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
                        new ActualField("ClientToServer"), new FormalField(String.class) //TODO do we need all these strings?
                        , new FormalField(String.class), new FormalField(String.class));
                System.out.println("Someone wrote to serverSpace!, my space is: " + _fromClientSpace.toString());
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "removeClient" -> {
                        //System.out.println("Removing client!");
                        //_server.removeClient(tuple[1].toString());
                    }
                    case "clientIsReady" -> _server.clientIsReady(data[0]);
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

class GameReceiver implements Runnable{
    ServerMain _server;
    SequentialSpace _fromGameSpace;

    public GameReceiver(ServerMain server, SequentialSpace fromGameSpace){
        _server = server;
        _fromGameSpace = fromGameSpace;
    }

    @Override
    public void run() { //TODO everything below should be rewrote.
        while (true) {
            try {
                Object[] tuple = _fromGameSpace.get(
                        new ActualField("GameToServer"), new FormalField(String.class)
                        , new FormalField(String.class), new FormalField(String.class));
                System.out.println("Someone wrote to serverSpace!, my space is: " + _fromGameSpace.toString());
                String[] data = tuple[3].toString().split(",");
                switch (tuple[2].toString()) {
                    case "addNewPlayer" -> { }
                    case "removePlayer" -> { }
                    case "updateMovement" -> {}
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}