package TapasGames.Chat;

import JspaceFiles.jspace.*;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatController {
    private HashMap<String, Thread> _rooms;
    private SpaceRepository _repository;
    private SequentialSpace _serverSpace;
    private SequentialSpace _chatRoomSpace;

    public ChatController(SpaceRepository repository, SequentialSpace serverSpace) {
        _rooms = new HashMap<>();
        _repository = repository;
        _serverSpace = serverSpace;
        _chatRoomSpace = new SequentialSpace();
        new Thread(new ServerReceiver(this, _serverSpace)).start();
    }

    public void addChatRoom(String id) {
        System.out.println("ChatController creating new chatRoom id: " + id);
        SequentialSpace toChatRoomSpace = new SequentialSpace();
        _repository.add("toChatRoom:" + id, toChatRoomSpace);
        _rooms.put(id, new Thread(new ChatRoom(id, _repository, toChatRoomSpace, _chatRoomSpace)));
        _rooms.get(id).start();
        try {
            _chatRoomSpace.get(new ActualField("ChatRoomBackToController"), new ActualField(id + "Created"));
            _serverSpace.put("ChatBackToServer", "ChatRoomAdded"); //THis is correct
            System.out.println("ChatController have created ChatRoom");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClient(String name, String id) {
        try {
            _repository.get("toChatRoom:" + id).put("addClient", name);
            _chatRoomSpace.get(new ActualField("ChatRoomBackToController"), new ActualField(id + name + "Added"));
            _serverSpace.put("ChatBackToServer", "ClientAdded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String name, String id) {
        try {
            _repository.get("toChatRoom:" + id).put("removeClient", name);
            _chatRoomSpace.get(new ActualField("ChatRoomBackToController" + id + name + "Removed"));
            _serverSpace.put("ChatBackToServer", "ClientRemoved");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class ServerReceiver implements Runnable {
    private ChatController _controller;
    private SequentialSpace _serverSpace;

    public ServerReceiver(ChatController controller, SequentialSpace serverSpace) {
        _controller = controller;
        _serverSpace = serverSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _serverSpace.get(
                        new ActualField("ServerToChat"), new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addChatRoom" -> _controller.addChatRoom(data[0]);
                    case "addClient" -> _controller.addClient(data[0], data[1]);
                    case "removeClient" -> _controller.removeClient(data[0], data[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ChatRoom implements Runnable {
    private ArrayList<String> _clients;
    private SpaceRepository _repository;
    private SequentialSpace _toChatRoomSpace;
    private SequentialSpace _controllerSpace;
    private String _id;

    ChatRoom(String id, SpaceRepository repository, SequentialSpace toChatRoomSpace, SequentialSpace controllerSpace) {
        _id = id;
        _repository = repository;
        _toChatRoomSpace = toChatRoomSpace;
        _controllerSpace = controllerSpace;
        _clients = new ArrayList<>();
        try {
            _controllerSpace.put("ChatRoomBackToController", _id + "Created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addClient(String client) {
        System.out.println("ChatRoom adding client: " + client + " : " + _id);
        _clients.add(client);
        try {
            _controllerSpace.put("ChatRoomBackToController", _id + client + "Added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String client) {
        _repository.remove("ChatToClient:" + client);
        _clients.remove(client);
        try {
            _controllerSpace.put("ChatRoomBackToController", _id + "Removed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToAll(String name, String message) {
        System.out.println("ChatRoom sending to all: " + name + " : " + message);
        for (String client : _clients) {
            Space toClientSpace = _repository.get("ChatToClient:" + client);
            try {
                System.out.println("Sending message from: " + name + " to: " + client);
                toClientSpace.put(name + "," + _id + "," + message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _toChatRoomSpace.get(
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[1].toString().split(",");
                switch (tuple[0].toString()) {
                    case "sendMessage" -> sendMessageToAll(data[0], data[1]);
                    case "addClient" -> addClient(data[0]);
                    case "removeClient" -> removeClient(data[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}