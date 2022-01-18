package TapasGames.Game;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.RemoteSpace;
import JspaceFiles.jspace.SequentialSpace;
import TapasGames.Client.ClientMain;

import java.util.HashMap;

public class GamesController {
    private HashMap<String, String> playerDic;
    private int[] indexs = new int[4];
    private SequentialSpace _serverSpace;

    public GamesController(SequentialSpace serverSpace) {
        _serverSpace = serverSpace;

        playerDic = new HashMap<>();
    }

    public void AddNewPlayer(String name) {
        for (int i = 0; i < 4; i ++) {
            if (indexs[i] == 0) {
                playerDic.put(name, "" + (i + 1));
                indexs[i] += 1;
                break;
            }
        }

        try {
            _serverSpace.put("toServer", "fromGame", name + " has been added", playerDic.get(name));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void RemovePlayer(String name) {
        String value = playerDic.get(name);
        playerDic.remove(name);
        indexs[(Integer.parseInt(value) - 1)] -= 1;

        try {
            _serverSpace.put("toServer", "fromGame", name + " has been removed");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    case "addNewPlayer" -> {_gamesController.AddNewPlayer("Kp dum");}
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
