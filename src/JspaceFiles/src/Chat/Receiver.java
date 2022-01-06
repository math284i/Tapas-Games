package JspaceFiles.src.Chat;

import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.Space;

public class Receiver implements Runnable {
    private Space chat;
    private String name;

    public Receiver(Space chat, String name) {
        this.chat = chat;
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object[] t = chat.query(new FormalField(String.class), new FormalField(String.class));
                System.out.println(t[0] + " says: " + t[1]);
            } catch (InterruptedException e) {
                System.out.println("Receiver caught an error!");
            }
        }
    }
}
