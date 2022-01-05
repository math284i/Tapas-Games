package common.src.Chat;

import org.jspace.Space;

import java.util.Scanner;

public class Sender implements Runnable {
    private Space chat;
    private String name;
    private Scanner txtInput;

    public Sender(Space chat, String name, Scanner txtInput) {
        this.chat = chat;
        this.name = name;
        this.txtInput = txtInput;
    }

    @Override
    public void run() {
        String message = "";

        while (!message.equals("End")) {
            message = txtInput.nextLine();
            try {
                chat.put(name, message);
            } catch (InterruptedException ignored) { }
        }
    }
}
