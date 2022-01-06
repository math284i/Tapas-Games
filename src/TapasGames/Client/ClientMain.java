package TapasGames.Client;

import JspaceFiles.jspace.RemoteSpace;
import TapasGames.UI.UIController;

public class ClientMain implements Runnable{
    private RemoteSpace _space;
    private UIController _ui;
    private String _name;
    private String _ip;

    public ClientMain(UIController ui, String name, String ip) {
        _ui = ui;
        _name = name;
        _ip = ip;
        try {
            _space = new RemoteSpace(_ip);
        } catch (Exception e) {
            System.out.println("Failed while trying to connect to IP :" + _ip + "\n with " + e);
        }
    }

    @Override
    public void run() {

    }
}
