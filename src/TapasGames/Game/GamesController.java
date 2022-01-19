package TapasGames.Game;

import JspaceFiles.jspace.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamesController {
    private HashMap<String, String> _playerDic;
    private int[] indexs = new int[4];
    private SpaceRepository _repository;
    private SequentialSpace _serverSpace;
    private SequentialSpace _toVotingSpace;
    private SequentialSpace _toGameRoomSpace;
    private String _currentlyPlaying;
    private ArrayList<String> _votingList;
    private ArrayList<String> _peopleThatWantToSkipGame;
    private Thread _gameRoomThread;

    public GamesController(SpaceRepository repository, SequentialSpace serverSpace) {
        _repository = repository;
        _serverSpace = serverSpace;
        _playerDic = new HashMap<>();
        _currentlyPlaying = "lobby";
        _votingList = new ArrayList<>();
        _peopleThatWantToSkipGame = new ArrayList<>();
        _toGameRoomSpace = new SequentialSpace();
        _repository.add("toGameRoom:", _toGameRoomSpace);
        _repository.add("toVoting:", _toVotingSpace);
        new Thread(new ServerReceiver(this, _serverSpace)).start();
        new Thread(new VoteController(this, _toVotingSpace)).start();
        _gameRoomThread = new Thread(new GameRoom(this, _toGameRoomSpace));
        _gameRoomThread.start();
    }

    public String getCurrentlyPlaying() {
        return _currentlyPlaying;
    }

    public HashMap<String, String> getPlayerDic() {
        return _playerDic;
    }

    public void AddNewPlayer(String name) {
        for (int i = 0; i < 4; i++) {
            if (indexs[i] == 0) {
                _playerDic.put(name, "" + (i + 1));
                indexs[i] += 1;
                break;
            }
        }
        try {
            _serverSpace.put("GameBackToServer", name + " has been added", "" + _playerDic.get(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RemovePlayer(String name) {
        String value = _playerDic.get(name);
        _playerDic.remove(name);
        indexs[(Integer.parseInt(value) - 1)] -= 1;
        try {
            _serverSpace.put("GameBackToServer", name + " has been removed");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendUpdateToAll(String data) {
        System.out.println("GameRoom sending to all: " + data);
        for (var entry : _playerDic.entrySet()) {
            try {
                System.out.println("GameRoom sending: " + entry.getKey() + "the data" + data);
                _toGameRoomSpace.put("GameRoomToClient", entry.getKey(), "updateGame", data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void votingTime() {
        System.out.println("GameRoom telling everyone its votingtime!");
        for (var entry : _playerDic.entrySet()) {
            try {
                System.out.println("GameRoom telling: " + entry.getKey() + " to vote for trump!");
                _toGameRoomSpace.put("GameRoomToClient", entry.getKey(), "votingTime", "");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addVotingResult(String name, String result) {
        _votingList.add(result);
        System.out.println("Game received: " + name + " chose: " + result);

        if (_votingList.size() == _playerDic.size()) {
            System.out.println("Game got all the votes needed!");
            String newGame = mostCommon(_votingList);
            System.out.println("Most voted game was: " + newGame);
            _votingList.clear();

            for (var entry : _playerDic.entrySet()) {
                try {
                    _toGameRoomSpace.put("GameRoomToClient", entry.getKey(), "newGame", newGame);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void rockTheVote(String name){
        if (_peopleThatWantToSkipGame.contains(name)) System.out.println("You have already voted from server");
        else {
            _peopleThatWantToSkipGame.add(name);
            if (_peopleThatWantToSkipGame.size() == _playerDic.size()) {
                System.out.println("Everyone wants to skip!");
                votingTime();
                _peopleThatWantToSkipGame.clear();
            } else {
                System.out.println("There is: " + _peopleThatWantToSkipGame.size() + " ready to vote out of: " + _playerDic.size());
            }

        }
    }

    //CODE COMES FROM: https://stackoverflow.com/questions/19031213/java-get-most-common-element-in-a-list
    public <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Map.Entry<T, Integer> max = null;

        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }

        return max == null ? null : max.getKey();
    }

    public void UpdatePlayers(String data) {
        //Name, left, right, up, down, mouseClick, x, y


    }

}

class GameRoom implements Runnable {
    private GamesController _gamescontroller;
    private SequentialSpace _toGameRoomSpace;

    public GameRoom(GamesController gamescontroller, SequentialSpace toGameRoomSpace) {
        _gamescontroller = gamescontroller;
        _toGameRoomSpace = toGameRoomSpace;
    }

    @Override
    public void run() {
        while (true) {
            StringBuilder data = new StringBuilder();
            for (var entry : _gamescontroller.getPlayerDic().entrySet()) {
                try {
                    Object[] tuple = _toGameRoomSpace.get(new ActualField("ClientToGameRoom"),
                            new ActualField(entry.getKey()), new FormalField(String.class));
                    data.append(tuple[2].toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            _gamescontroller.sendUpdateToAll(data.toString());
        }
    }
}

class VoteController implements Runnable {

    private GamesController _gamescontroller;
    private SequentialSpace _toVotingSpace;

    public VoteController(GamesController gamescontroller, SequentialSpace toVotingSpace) {
        _gamescontroller = gamescontroller;
        _toVotingSpace = toVotingSpace;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] tuple = _toVotingSpace.get(
                        new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[1].toString().split(",");
                switch (tuple[0].toString()) {
                    case "votingTime" -> _gamescontroller.votingTime();
                    case "votingResult" -> _gamescontroller.addVotingResult(data[0], data[1]);
                    case "rockTheVote" -> _gamescontroller.rockTheVote(data[0]);

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
                        , new FormalField(String.class), new FormalField(String.class));
                String[] data = tuple[2].toString().split(",");
                switch (tuple[1].toString()) {
                    case "addNewPlayer" -> _gamesController.AddNewPlayer(data[0]);
                    case "removePlayer" -> _gamesController.RemovePlayer(data[0]);
                    case "votingTime" -> _gamesController.votingTime();
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
