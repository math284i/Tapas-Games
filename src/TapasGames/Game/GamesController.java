package TapasGames.Game;

import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.SequentialSpace;
import JspaceFiles.jspace.SpaceRepository;

import java.util.HashMap;

public class GamesController {
    private HashMap<String, String> playerDic;
    private int[] indexs = new int[4];
    private SpaceRepository _repository;
    private SequentialSpace _serverSpace;
    private SequentialSpace _gameRoomSpace;
    private SequentialSpace _toGameRoomSpace;
    private String currentlyPlaying = "lobby";
    private Thread gameRoomThread;

    public GamesController(SpaceRepository repository, SequentialSpace serverSpace) {
        _repository = repository;
        _serverSpace = serverSpace;
        _gameRoomSpace = new SequentialSpace();
        playerDic = new HashMap<>();
        _toGameRoomSpace = new SequentialSpace();
        _repository.add("toGameRoom:", _toGameRoomSpace);
        new Thread(new ServerReceiver(this, _serverSpace)).start();
        gameRoomThread = new Thread(new GameRoom(this, _toGameRoomSpace, _gameRoomSpace));
        gameRoomThread.start();
    }

    public void AddNewPlayer(String name) {
        for (int i = 0; i < 4; i ++) {
            if (indexs[i] == 0) {
                playerDic.put(name, "" + (i + 1));
                try {
                    _toGameRoomSpace.put("addPlayer", name + "," + (i+1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                indexs[i] += 1;
                break;
            }
        }
        try {
            _serverSpace.put("GameBackToServer", name + " has been added", "" + playerDic.get(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemovePlayer(String name) {
        String value = playerDic.get(name);
        playerDic.remove(name);
        indexs[(Integer.parseInt(value) - 1)] -= 1;
        try {
            _toGameRoomSpace.put("removePlayer", name);
            _serverSpace.put("GameBackToServer", name + " has been removed");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void votingTime() {
        System.out.println("GameController saying its votingTime!");
        try {
            _toGameRoomSpace.put("votingTime", "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void UpdatePlayers(String data) {
        //Name, left, right, up, down, mouseClick, x, y

        switch (currentlyPlaying) {
            case "lobby" -> {}
            case "curvefever" -> {}
            case "minesweeper" -> {}
        }

    }

}

class GameRoom implements Runnable {
    private HashMap<String, String> _playerDic;
    private GamesController _gamescontroller; //TODO Not sure this is needed - figure out!
    private SequentialSpace _toGameRoomSpace;
    private SequentialSpace _controllerSpace; //TODO Not sure this is needed - figure out!

    public GameRoom(GamesController gamescontroller, SequentialSpace toGameRoomSpace, SequentialSpace gameRoomSpace) {
        _playerDic = new HashMap<>();
        _gamescontroller = gamescontroller;
        _toGameRoomSpace = toGameRoomSpace;
        _controllerSpace = gameRoomSpace;
    }

    private void sendUpdateToAll() {

    }

    private void addPlayer(String name, String number) {
        System.out.println("GameRoom AddingPlayer: " + name);
        _playerDic.put(name, number);
    }

    private void removePlayer(String name) {
        _playerDic.remove(name);
    }

    private void votingTime() {
        System.out.println("GameRoom telling everyone its votingtime!");
        for (var entry : _playerDic.entrySet()) {
            try {
                System.out.println("GameRoom telling: " + entry.getKey() + " to vote for trump!");
                _toGameRoomSpace.put("GameRoomToClient",entry.getKey(), "votingTime", "");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addVotingResult(String name, String result) {
        System.out.println("Game received: " + name + " chose: " + result);

    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _toGameRoomSpace.get(
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[1].toString().split(",");
                switch (tuple[0].toString()) {
                    case "addPlayer" -> addPlayer(data[0], _playerDic.get(data[0]));
                    case "removePlayer" -> removePlayer(data[0]);
                    case "votingTime" -> votingTime();
                    case "votingResult" -> addVotingResult(data[0], data[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerReceiver implements Runnable {
    private GamesController _gamesController;
    private SequentialSpace _fromServerSpace;

    public ServerReceiver(GamesController gamesController, SequentialSpace fromServerSpace) {
        _gamesController = gamesController;
        _fromServerSpace = fromServerSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _fromServerSpace.get(new ActualField("ServerToGame")
                        , new FormalField(String.class), new FormalField(String.class)); // data = "Name, left, right, up, down, leftClick, x, y"
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addNewPlayer" -> _gamesController.AddNewPlayer(data[0]);
                    case "removePlayer" -> _gamesController.RemovePlayer(data[0]);
                    case "updateMovement" -> { }
                    case "votingTime" -> _gamesController.votingTime();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
