package blockchain;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application{
    public static ArrayList<Block> BlocktoVerify = new ArrayList<>();
    public static ArrayList<Block> BlockChain = new ArrayList<>();
    public static int BlockId = 0;
    private static TextField senderField;
    private static TextField receiverField;
    private static TextField amountField;


    public static void main(String[] args ) throws InterruptedException{
        Runnable RunnableMiner = () -> {
            while (true){
                synchronized (BlocktoVerify){
                if (BlocktoVerify.size() > 0){
                    try {
                        Mining.StartMining();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Mining failed");
                    }
                }
            }
        }
    };
        Thread MinerThread = new Thread(RunnableMiner);
        MinerThread.start();
        launch(args);
}


    @Override
    public void start(Stage primaryStage) {
        senderField = new TextField();
        senderField.setPromptText("Sender");

        receiverField = new TextField();
        receiverField.setPromptText("Receiver");

        amountField = new TextField();
        amountField.setPromptText("Amount");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            try {
                Transfer.Send(senderField.getText(), receiverField.getText(), amountField.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        VBox root = new VBox(20);
        root.getChildren().addAll(senderField, receiverField, amountField, sendButton);

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Transaction GUI");
        primaryStage.show();
    }

    
}