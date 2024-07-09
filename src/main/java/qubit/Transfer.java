package qubit;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.ArrayList;

import static qubit.App.BlocktoVerify;
import static qubit.App.BlockId;;


public class Transfer{
    // private static final Exception NO_ENOUGH_BALANCE_EXCEPTION = null;
    // static PublicKey pubkeySend;
    // static PrivateKey prikeySend;
    public static ArrayList<Dataclass> DataList = new ArrayList<>();


    // Verifying if sender has enough balence to send the amount
    protected static Boolean VerifyTransaction(int SenderID,int ReceiverID, double Amount) throws SQLException {
        return SenderID == -1 || ReceiverID == -1 || DBFuncs.getUserBalence(SenderID) - Amount < 0 ? false : true; 
    }

    // All operations related to sending money
    public static void Send(String Sender, String Receiver, String Amount_in_String) throws Exception{
        double Amount = Double.parseDouble(Amount_in_String);
        int SenderID = DBFuncs.getUserId(Sender);
        int ReceiverID = DBFuncs.getUserId(Receiver);
        if (SenderID == -1) DBFuncs.register(Sender); 
        if (ReceiverID == -1) DBFuncs.register(Receiver);
        SenderID = DBFuncs.getUserId(Sender);
        ReceiverID = DBFuncs.getUserId(Receiver);
        if (VerifyTransaction(SenderID, ReceiverID, Amount)) {
            DBFuncs.UpdateBalence(SenderID, -Amount);
            DBFuncs.UpdateBalence(ReceiverID, Amount);
        }
            
        Transaction transaction = new Transaction(SenderID, ReceiverID, Amount);
        // Getting the public and private keys of the sender
        PublicKey pubkeySend = DBFuncs.getPublicKey(SenderID);
        PrivateKey prikeySend = DBFuncs.getPrivateKey(SenderID);

        // Signing the transaction so that it can be verified using sender's public key 
        byte[] signature = Utils.Sign(transaction.toString(), prikeySend); 
        BlockId++;
        if (Utils.VerifyTransaction(signature, transaction.toString(), pubkeySend)){
            // checked if the sender has enough balence
            // one transaction per block
            Block block = new Block(transaction, BlockId);
            synchronized (BlocktoVerify){
                BlocktoVerify.add(block);
                block.PrintBlock();
            }
        }
    }
}