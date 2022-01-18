package TapasGames.Game;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.RemoteSpace;
import JspaceFiles.jspace.SequentialSpace;
import TapasGames.Client.ClientMain;

import java.util.HashMap;

public class GamesController {
    private HashMap<String, String> playerDic;
    private SequentialSpace _serverSpace;

    public GamesController(SequentialSpace serverSpace) {
        _serverSpace = serverSpace;

        playerDic = new HashMap<>();
        playerDic.put("Placeholder1", "1");
        playerDic.put("Placeholder2", "2");
        playerDic.put("Placeholder3", "3");
        playerDic.put("Placeholder4", "4");
    }
}

class ServerReceiver implements Runnable {
    private GamesController _gamesController;
    private RemoteSpace _fromServerSpace;

    public ServerReceiver(GamesController gamesController, RemoteSpace fromServerSpace) {
        _gamesController = gamesController;
        _fromServerSpace = fromServerSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromServerSpace.get(new ActualField("ToGame"), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addNewPlayer" -> {}
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
