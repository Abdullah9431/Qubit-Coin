package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.ArrayList;
import java.io.IOException;

import static blockchain.App.BlocktoVerify;
import static blockchain.App.BlockId;;


public class Transfer{
    static PublicKey pubkeySend;
    static PrivateKey prikeySend;
    public static ArrayList<Dataclass> DataList = new ArrayList<>();



    protected static void VerifyTransaction(String Sender,String Receiver, String AmountInString) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        int SenderID = DBFuncs.getUserId(1, Sender);
        int ReceiverID = DBFuncs.getUserId(1, Receiver);

        // DataList is Currently using as database
        if (SenderID == -1) DataList.add(DBFuncs.register(Sender));
        if (ReceiverID  == -1) DataList.add(DBFuncs.register(Receiver));
        double Amount = Double.parseDouble(AmountInString);

        // Verifying if sender has enough balence
        if(DBFuncs.getUserBalence(SenderID) - Amount >= 0) Send(SenderID, ReceiverID, Amount);
    }

    

    public static void Send(int SenderID, int ReceiverID, double Amount) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException{
        try{
            for (Dataclass data : DataList) {
                if (SenderID == data.Id) {
                    data.balance -= Amount;
                    prikeySend = data.privkey;
                    pubkeySend = data.pubkey;
                    System.out.println(data);
                }
                else if (ReceiverID == data.Id) {
                        data.balance += Amount;
                        System.out.println(data);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Transaction transaction = new Transaction(SenderID, ReceiverID, Amount);
        byte[] signature = Utils.Sign(transaction.toString(), prikeySend);
        BlockId++;
        if (Utils.VerifyTransaction(signature, transaction.toString(), pubkeySend)){

            // one transaction per block
            Block block = new Block(transaction, BlockId);
            synchronized (BlocktoVerify){
                BlocktoVerify.add(block);
                block.PrintBlock();
            }
        }
    }
}