package TapasGames.Chat;

import JspaceFiles.jspace.*;

import java.util.ArrayList;

public class ChatController{
    private ArrayList<Thread> _rooms;
    private SpaceRepository _repository;

    public ChatController(SpaceRepository repository) {
        _repository = repository;
        _rooms = new ArrayList<>();
    }

    public void addChatRoom(int id){
        SequentialSpace toChatRoomSpace = new SequentialSpace();
        _repository.add("toChatRoom: "+id,toChatRoomSpace);
        _rooms.add(new Thread(new ChatRoom(id, _repository,toChatRoomSpace)));
        _rooms.get(id).start();
    }

}

class ChatRoom implements Runnable {
    private ArrayList<String> _clients;
    private SpaceRepository _repository;
    private SequentialSpace _toChatRoomSpace;
    private int _id;

    ChatRoom(int id, SpaceRepository repository, SequentialSpace toChatRoomSpace){
        _id = id;
        _repository = repository;
        _toChatRoomSpace = toChatRoomSpace;
    }

    public void removeClient(String client) {
        _repository.remove("ChatToClient: "+client);
        _clients.remove(client);
    }

    public void addClient(String client) {
        SequentialSpace space = new SequentialSpace();
        _repository.add("ChatToClient: "+client, space);
        _clients.add(client);
    }

    private void sendMessageToAll(String name, String message) {
        for (String client : _clients) {
            Space toClientSpace = _repository.get("ChatToClient: "+client);
            try {
                toClientSpace.put(name, message);
            } catch (InterruptedException e) {
                System.out.println("ChatController failed to send message to: " + client + " with " + e);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] data = _toChatRoomSpace.get(
                        new FormalField(Integer.class), new FormalField(String.class), new FormalField(String.class));
                if((int) data[0] == 0) {
                    sendMessageToAll(data[1].toString(), data[2].toString());
                } else if((int) data[0] == 1) {
                    addClient(data[1].toString());
                } else if((int) data[0] == 2) {
                    removeClient(data[1].toString());
                }
            } catch (InterruptedException ignored) {}
        }
    }

}