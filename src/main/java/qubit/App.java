package qubit;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class App extends Application{
    public static ArrayList<Block> BlocktoVerify = new ArrayList<>();
    public static ArrayList<Block> BlockChain = new ArrayList<>();
    public static int BlockId = 0;
    private static TextField senderField;
    private static TextField receiverField;
    private static TextField amountField;


    public static void main(String[] args) throws Exception{
        if (args[0].equals("true")){
            Transfer.Send("Abdullah", "Khalid", "1000");
        } else {
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
}


    @Override
    public void start(Stage primaryStage) {
        senderField = new TextField();
        senderField.setPromptText("Sender");
        senderField.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY)));

        receiverField = new TextField();
        receiverField.setPromptText("Receiver");
        receiverField.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY)));

        amountField = new TextField();
        amountField.setPromptText("Amount");
        amountField.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, CornerRadii.EMPTY, Insets.EMPTY)));


        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            try {
                Transfer.Send(senderField.getText(), receiverField.getText(), amountField.getText());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        VBox root = new VBox(50);
        root.getChildren().addAll(senderField, receiverField, amountField, sendButton);

        Stop[] stops = new Stop[] { new Stop(0, Color.BLUE), new Stop(0.5, Color.LIGHTBLUE), new Stop(1, Color.PINK)};
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        
        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(root); // Add VBox to the center of BorderPane
        mainPane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(mainPane, 1000, 1000);
        scene.setFill(gradient);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Transaction GUI");
        primaryStage.show();
    }
}