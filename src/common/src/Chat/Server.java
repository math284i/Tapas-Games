package common.src.Chat;

import org.jspace.FormalField;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

public class Server {

    public static void main(String[] args) {
        System.out.println("Server Starting...");
        try {
            SequentialSpace chat = Start();
            System.out.println("Server Started!");
            ReadChatMessages(chat);
        } catch (InterruptedException e) {
            System.out.println("Server crashed!");
        }
    }

    public static SequentialSpace Start() throws InterruptedException {
        SpaceRepository chatRepository = new SpaceRepository();
        SequentialSpace chat = new SequentialSpace();
        chatRepository.add("AllChat", chat);
        chatRepository.addGate("tcp://localhost:31415/?keep");
        return chat;
    }

    private static void ReadChatMessages(SequentialSpace chatSpace) throws InterruptedException {
        while (true) {
            Object[] t = chatSpace.query(new FormalField(String.class), new FormalField(String.class));
            System.out.println(t[0] + ":" + t[1]);
        }
    }

}
