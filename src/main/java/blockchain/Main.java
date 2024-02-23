package blockchain;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Main extends Application{
    public static ArrayList<Dataclass> DataList = new ArrayList<>();
    public static ArrayList<Block> BlocktoVerify = new ArrayList<>();
    public static ArrayList<Block> VerifiedBlock = new ArrayList<>();
    public static int BlockId = 0;
    static PublicKey pubkeySend;
    static PrivateKey prikeySend;
    static Lock lock = new ReentrantLock();
    public static byte[] prevhash;
    private static TextField senderField;
    private static TextField receiverField;
    private static TextField amountField;

    
    private static void sendTransaction() throws NoSuchAlgorithmException, IOException {
        String sender = senderField.getText();
        String receiver = receiverField.getText();
        if (Register.getUserId(sender) == -1) DataList.add(Register.register(sender));
        if (Register.getUserId(receiver) == -1) DataList.add(Register.register(receiver));
        double amount = Double.parseDouble(amountField.getText());
        try {
            send(sender, receiver, amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args ) throws NoSuchAlgorithmException, IOException, SQLException, InvalidKeyException, SignatureException, InterruptedException{
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
        // send(Sender, Receiver, 100.0);
        // send(Receiver, Sender, 500.0);
        

    }

    public static void send(String sender, String receiver, double Amount) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException{
        //SEND MONEY
        int IdSend = Register.getUserId(sender);
        int IdRecv = Register.getUserId(receiver);
        
        try{
            for (Dataclass data : DataList) {
                if (IdSend == data.Id) {
                    
                    if (data.balance < Amount) {System.out.println("Balance not enough for user id: " + data.Id);continue;}
                    data.balance -= Amount;
                    System.out.println(data);
                }
                else if (IdRecv == data.Id) {
                        data.balance += Amount;
                        System.out.println(data);
                }
            }
        } catch (Exception e){
            System.out.println("Transaction Failed");
        }
        Transaction transaction = new Transaction(IdSend, IdRecv, Amount);
        byte[] signature = Utils.Sign(transaction.toString(), prikeySend);
        BlockId++;
        if (Utils.VerifyTransaction(signature, transaction.toString(), pubkeySend)){
            Block block = new Block(transaction, BlockId);
            synchronized (BlocktoVerify){
                BlocktoVerify.add(block);
                block.PrintBlock();
            }
        }
    }

    // public static void mineBlocks() throws InterruptedException, NoSuchAlgorithmException{
    //     System.out.println("Mining started...");
    //     System.out.println("Verified block size: " + VerifiedBlock.size());
    //     Block block = null;
    //     if (VerifiedBlock.size() > 0) {
    //         block = VerifiedBlock.get(VerifiedBlock.size() - 1);
    //         block.prevHash = prevhash;
    //         System.out.println("Block hash setted: " + block.prevHash.hashCode());
    //     }
    //     Block CurrentBlock = BlocktoVerify.remove(0);
    //     VerifiedBlock.add(CurrentBlock);
    //     String content = CurrentBlock.content;
    //     MessageDigest digest = MessageDigest.getInstance("SHA-256");
    //     prevhash = digest.digest(content.getBytes());
    //     if(CurrentBlock.BlockId > 1) {System.out.println("Blockid: " + CurrentBlock.BlockId);System.out.println(block.prevHash.hashCode());}
    //     System.out.println("Block mined: " + CurrentBlock.BlockId);
            
    //     // Process the block once it's available
        
    //     // System.out.println("Block mined: " + block);
    // }

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
                sendTransaction();
            } catch (NoSuchAlgorithmException | IOException e1) {
                e1.printStackTrace();
            }
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(senderField, receiverField, amountField, sendButton);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Transaction GUI");
        primaryStage.show();
    }
}      