package TapasGames.Chat;

import JspaceFiles.jspace.*;
import JspaceFiles.src.Chat.Client;
import TapasGames.Client.ClientMain;

import java.util.ArrayList;

public class ChatController implements Runnable{
    private ArrayList<ClientMain> _clients;
    private SequentialSpace _serverSpace;
    private SpaceRepository _spaceRepository;
    private String _ipWithPort;

    public ChatController(SequentialSpace serverSpace, String ipWithPort) {
        _serverSpace = serverSpace;
        _ipWithPort = ipWithPort;

        _clients = new ArrayList<>();
        _spaceRepository = new SpaceRepository();
        _spaceRepository.addGate(_ipWithPort + "?keep");
    }

    public void removeClient(ClientMain client) {
        _spaceRepository.remove(client.getName());
        _clients.remove(client);
    }

    public void addClient(ClientMain client) {
        SequentialSpace space = new SequentialSpace();
        _spaceRepository.add(client.getName(), space);
        _clients.add(client);
    }

    private void sendMessageToAll(String name, String message) {
        for (ClientMain client : _clients) {
            Space space = _spaceRepository.get(client.getName());
            try {
                space.put(name, message);
            } catch (InterruptedException e) {
                System.out.println("ChatController failed to send message to: " + client.getName() + " with " + e);
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] nameAndMessage = _serverSpace.get(
                        new FormalField(String.class), new FormalField(String.class));
                sendMessageToAll(nameAndMessage[0].toString(), nameAndMessage[1].toString());

            } catch (InterruptedException ignored) {}
        }
    }

}
