package TapasGames.Client;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.RemoteSpace;
import TapasGames.UI.UIController;

import java.util.Scanner;

public class ClientMain {
    private RemoteSpace _space;
    private UIController _ui;
    private String _name;
    private String _serverIp;
    private String _senderIp;
    private String _receiverIp;

    public ClientMain(UIController ui, String name, String ip) {
        _ui = ui;
        _name = name;
        _serverIp = ip;

        try {
            _space = new RemoteSpace(_serverIp);
        } catch (Exception e) {
            System.out.println("Failed while trying to connect to IP: " + _serverIp + "\n with: " + e);
        }
        try {
            Object[] chatIps = _space.get(new ActualField(name), new ActualField("chatIps")
                    , new FormalField(String.class), new FormalField(String.class));
            _senderIp = chatIps[2].toString();
            _receiverIp = chatIps[3].toString();

        } catch (Exception e) {
            System.out.println("Failed to get chatIps with: " + e);
        }


        new Thread(new ChatSender(this, _senderIp)).start();
        new Thread(new ChatReceiver(this, _receiverIp)).start();
    }

    public String getName() {
        return _name;
    }

    public String getReceiverIp() {
        return _receiverIp;
    }

    public void updateChatUI(String name, String message) {
        System.out.println(name + " Says: " + message);
    }
}

class ChatSender implements Runnable {
    private ClientMain _client;
    private String _senderIp;
    private RemoteSpace _space;
    Scanner cSource = new Scanner(System.in);

    public ChatSender(ClientMain client, String senderIp) {
        _client = client;
        _senderIp = senderIp;
        try {
            _space = new RemoteSpace(_senderIp);
        } catch (Exception e) {
            System.out.println("Failed while trying to connect to IP: " + _senderIp + "\n with: " + e);
        }
    }

    @Override
    public void run() {
        String message = "";
        String type = "chat";
        while (true) {
            message = cSource.nextLine();
            try {
                _space.put(_client.getName(), message, type);
            } catch (InterruptedException ignored) {
            }
        }
    }
}

class ChatReceiver implements Runnable {
    private ClientMain _client;
    private String _receiverIp;
    private RemoteSpace _space;

    public ChatReceiver(ClientMain client, String receiverIp) {
        _client = client;
        _receiverIp = receiverIp;
        try {
            _space = new RemoteSpace(_receiverIp);
        } catch (Exception e) {
            System.out.println("Failed while trying to connect to IP: " + _receiverIp + "\n with: " + e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] t = _space.get(new FormalField(String.class), new FormalField(String.class));
                _client.updateChatUI(t[0].toString(), t[1].toString());
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}