package TapasGames.Server;

import JspaceFiles.jspace.RemoteSpace;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;
import TapasGames.Chat.ChatController;
import TapasGames.Client.ClientMain;
import TapasGames.UI.UIController;

import java.io.IOException;

public class ServerMain {
    private UIController _ui;
    private String _ip;
    private String _port;
    private String _ipWithPort;
    private ClientMain[] _clients;
    private SequentialSpace _space;
    private SequentialSpace _chatSpace;
    private SpaceRepository _repository;
    private String _senderIp;
    private String _receiverIp;

    public ServerMain(UIController ui, String ip, String port) {
        _ui = ui;
        _ip = ip;
        _port = port;
        _ipWithPort = "tcp://localhost:" + port + "/";
        _senderIp = _ipWithPort + "chatServer?keep";

        _repository = new SpaceRepository();
         _space = new SequentialSpace();
         _chatSpace = new SequentialSpace();
        _repository.add("clientConnections", _space);
        _repository.add("chatServer", _chatSpace);
        _repository.addGate(_ipWithPort + "?keep");
    }

    public void getIpsFromServer(ClientMain client) {
        try {
            _space.put(client.getName(), "chatIds", _senderIp, _receiverIp);
        } catch (InterruptedException e) {
            System.out.println("Server failed sending ips");
        }
    }

    //I need a MainController
    //Tell MainController im ready to receive connections!

}
