 package TapasGames;
import JspaceFiles.jspace.SequentialSpace;
import TapasGames.Client.ClientMain;
import TapasGames.UI.UIController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

 public class MainController {
     //STARTUP UI
     //CLIENT UI
     //SERVER UI

    //What u wanna be?
    //If client
    //New page - Get name and IP - check if valid name
    //Repository
    //New ClientMain(NEW UICONTROLLER(CLIENT UI), name, ip, repository)

    //if server
    //New ServerMain(NEW UICONTROLLER(SERVER UI), repository)

    public void onPressClient(ActionEvent actionEvent) throws IOException {
        System.out.println("Client is being created!");
        SequentialSpace space = new SequentialSpace();
        UIController ui = new UIController(space);
        Node node = (Node) actionEvent.getSource();
        node.getScene().getWindow().hide();
        new ClientMain(ui, "Name", "IpWithPort", space);
    }

    public void onPressServer(ActionEvent actionEvent) {
        System.out.println("Server is being created!");
    }
}
