package common.src.Chat;

import org.jspace.RemoteSpace;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = scanner.nextLine();
        RemoteSpace chat = new RemoteSpace("tcp://localhost:31415/AllChat?keep");

        new Thread(new Receiver(chat, name)).start();
        new Thread(new Sender(chat, name, scanner)).start();
    }
}
