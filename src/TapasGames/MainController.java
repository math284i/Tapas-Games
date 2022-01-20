 package TapasGames;
import JspaceFiles.jspace.ActualField;
import JspaceFiles.jspace.FormalField;
import JspaceFiles.jspace.RemoteSpace;
import JspaceFiles.jspace.SequentialSpace;
import TapasGames.Client.ClientMain;
import TapasGames.Server.ServerMain;
import TapasGames.Server.ServerUI;
import TapasGames.UI.UIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.UnknownHostException;

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
        System.out.println("Client is being created! part 1");

        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        final Stage dialog = new Stage();
        dialog.initStyle(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        GridPane grid = new GridPane();
        Label name = new Label("Name: ");
        Label ip = new Label ("IP: ");
        Button cancel = new Button("Cancel");
        cancel.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        Button done = new Button("Done");
        done.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        TextField nameT = new TextField();
        nameT.setMaxWidth(Double.MAX_VALUE);
        TextField ipT = new TextField();
        ipT.setMaxWidth(Double.MAX_VALUE);

        grid.setPrefSize(Double.MAX_VALUE,Double.MAX_VALUE);
        grid.add(name,0,0);
        grid.add(ip,0,1);
        grid.add(nameT,1,0);
        grid.add(ipT,1,1);
        grid.add(done,1,2);
        grid.add(cancel,0,2);

        grid.setVgrow(name,Priority.ALWAYS);
        grid.setVgrow(ip,Priority.ALWAYS);
        grid.setHgrow(nameT,Priority.ALWAYS);
        grid.setHgrow(ipT,Priority.ALWAYS);
        grid.setHgrow(cancel,Priority.ALWAYS);
        grid.setHgrow(done,Priority.NEVER);
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10,10,10,10));

        Scene dialogScene = new Scene(grid, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                dialog.close();
            }
        });
        done.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    System.out.println("Connecting to Server");
                    RemoteSpace serverSpace = new RemoteSpace(ipT.getText()+"startUpServer?keep");
                    serverSpace.put("StartUpToServer","addClient",nameT.getText());
                    String bool = serverSpace.get(new ActualField("ServerBackToStartUp"), new ActualField(ipT.getText()),new FormalField(String.class))[2].toString();
                    boolean b = Boolean.parseBoolean(bool);
                    System.out.println("At b");
                    if (b){
                        dialog.close();
                        System.out.println("Client is being created! part 2");
                        SequentialSpace space = new SequentialSpace();
                        UIController ui = new UIController(space,nameT.getText());
                        stage.close();
                        try {
                            System.out.print("" + nameT.getText() + " " + ipT.getText());
                            new ClientMain(ui, "" + nameT.getText(), "" + ipT.getText(), space);
                        } catch (IOException ignored) {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onPressServer(ActionEvent actionEvent) {
        System.out.println("Server is being created!");
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
        ServerUI ui = new ServerUI();
        try {
            new ServerMain(ui, "tcp://localhost:","31415");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
