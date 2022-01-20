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
    private boolean _inGame = false;

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
        _repository.add("startUpServer", _startUpSpace);
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
        new Thread(new ChatReceiver(this, _chatSpace)).start();
        new Thread(new GameReceiver(this, _gameSpace)).start();
    }

    public void addClient(String name) throws InterruptedException {
        if (_clients.contains(name)) {
            System.out.println("Server not adding: " + name);
            _startUpSpace.put("ServerBackToStartUp", _ipWithPort, "false");
        } else {
            System.out.println("Server adding: " + name);
            _clients.add(name);
            SequentialSpace space = new SequentialSpace();
            _repository.add("ChatToClient:" + name, space);
            _startUpSpace.put("ServerBackToStartUp", _ipWithPort, "true");
            _clientSpace.get(new ActualField("ClientBackToServer"), new ActualField(name), new ActualField("ClientCreated"));
            System.out.println("ServerMain received clientCreated!");
            addClientToChatRoom(name, "Global");
            addClientToGame(name);
            _ui.AddClient(name);
        }
    }

    public void removeClient(String name) throws InterruptedException {
        removeClientFromChatRoom(name, "Global");

        _clients.remove(name);
        _ui.RemoveClient(name);
    }

    public void addClientToGame(String name) throws InterruptedException {
        _gameSpace.put("ServerToGame", "addNewPlayer", name);
        Object[] tuple = _gameSpace.get(new ActualField("GameBackToServer")
                , new ActualField(name + " has been added"), new FormalField(String.class));
        System.out.println(name + " has been added to game as number: " + tuple[2].toString());

        for (String client : _clients) {
            _clientSpace.put("ServerToClient", client, "updateLobby", "" + name + "," + tuple[2].toString() + "," + "Not ready");
        }

        for (var entry : _clientsAndNumbers.entrySet()) {
            _clientSpace.put("ServerToClient", name, "updateLobby"
                    , "" + entry.getKey() + "," + entry.getValue() + "," + "Not ready");
        }
        _clientsAndNumbers.put(name, tuple[2].toString());
    }

    public void createChatRoom(String id) throws InterruptedException {
        _chatSpace.put("ServerToChat", "addChatRoom", id);
        System.out.println("ServerMain asked chat controller to create chat room: " + id);
        _chatSpace.get(new ActualField("ChatBackToServer"), new ActualField("ChatRoomAdded"));
        System.out.println("Server received chatRoomAdded!");
        _ui.AddChat(id);
    }

    public void addClientToChatRoom(String name, String id) throws InterruptedException {
        System.out.println("Server Adding: " + name + " to: " + id);
        _chatSpace.put("ServerToChat", "addClient", name + "," + id);
        _clientSpace.put("ServerToClient", name, "addChatRoom", id);
        _chatSpace.get(new ActualField("ChatBackToServer"), //was fromChatRoom
                new ActualField("ClientAdded")); //Used to have small c, Used to have an extra field for a string.
        System.out.println("Client is added from serverMain");
        _clientSpace.get(new ActualField("ClientBackToServer"), new ActualField(name),
                new ActualField("ChatRoomAdded"));
        System.out.println("Everything is done in Server AddClientToChatRoom");
    }

    public void removeClientFromChatRoom(String name, String id) throws InterruptedException {
        System.out.println("Server Removing: " + name + " from: " + id);
        _chatSpace.put("ServerToChat", "removeClient", name + "," + id);
        _clientSpace.put("ServerToClient", name, "removeChatRoom", id);
        _chatSpace.get(new ActualField("ChatBackToServer"), //was fromChatRoom
                new ActualField("ClientRemoved")); //Used to have small c, Used to have an extra field for a string.
        System.out.println("Client is removed from serverMain");
        _clientSpace.get(new ActualField("ClientBackToServer"), new ActualField(name),
                new ActualField("ChatRoomRemoved"));
        System.out.println("Everything is done in Server RemoveClientToChatRoom");
    }

    public void clientIsReady(String name) throws InterruptedException {
        _readyClients.add(name);

        String playerNumber = _clientsAndNumbers.get(name);

        for (var entry : _clientsAndNumbers.entrySet()) {
            _clientSpace.put("ServerToClient", entry.getKey(), "updateLobby"
                    , "" + name + "," + playerNumber + "," + "Ready");
        }

        if (_readyClients.size() == _clients.size()) {
            System.out.println("All players are ready to RUUUUUMBLE!");
            _gameSpace.put("ServerToGame", "gameOver", "");
            _readyClients.clear();
        }
    }

    public void gameOver(String playersWon) throws InterruptedException {
        _gameSpace.put("ServerToGame", "gameOver", playersWon);
    }

    public void updateChat(String id, String name, String message) {
        System.out.println("Server udating its own chat!");
        _ui.UpdateChat(id, name, message);
    }

    public void gameChangedToLobby() throws InterruptedException {
        for (String name : _clients) {
            for (String client : _clients) {
                String number = _clientsAndNumbers.get(client);
                _clientSpace.put("ServerToClient", name, "updateLobby", "" + client + "," + number + "," + "Not ready");
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
                        new ActualField("ClientToServer")
                        , new FormalField(String.class), new FormalField(String.class));
                System.out.println("Someone wrote to serverSpace!, my space is: " + _fromClientSpace.toString());
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "clientIsReady" -> _server.clientIsReady(data[0]);
                    case "gameOver" -> _server.gameOver(data[0]);
                    case "clientLeaving" -> _server.removeClient(data[0]);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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

class ChatReceiver implements Runnable {
    ServerMain _server;
    SequentialSpace _fromChatSpace;

    public ChatReceiver(ServerMain server, SequentialSpace fromChatSpace) {
        _server = server;
        _fromChatSpace = fromChatSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromChatSpace.get(new ActualField("ChatToServer")
                        , new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                System.out.println("Server received something form chat");
                switch (tuple[1].toString()) {
                    case "sendMessage" -> _server.updateChat(data[0], data[1], data[2]);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class GameReceiver implements Runnable {
    ServerMain _server;
    SequentialSpace _fromGameSpace;

    public GameReceiver(ServerMain server, SequentialSpace fromGameSpace) {
        _server = server;
        _fromGameSpace = fromGameSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromGameSpace.get(new ActualField("GameToServer")
                        , new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                System.out.println("Server received something form game");
                switch (tuple[1].toString()) {
                    case "createTeamChat" -> {
                        _server.createChatRoom(data[0]);
                        _fromGameSpace.put("ServerBackToGame",data[0] + "Created");
                    }
                    case "addPlayerToTeamChat" -> {
                        _server.addClientToChatRoom(data[0], data[1]);
                        _fromGameSpace.put("ServerBackToGame","Player" + data[0] + "AddedTo" + data[1]);
                    }
                    case "gameChangedToLobby" ->_server.gameChangedToLobby();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}