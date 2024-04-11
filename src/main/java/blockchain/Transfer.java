package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.io.IOException;

import static blockchain.App.BlocktoVerify;
import static blockchain.App.BlockId;;


public class Transfer{
    private static final Exception NO_ENOUGH_BALANCE_EXCEPTION = null;
    static PublicKey pubkeySend;
    static PrivateKey prikeySend;
    public static ArrayList<Dataclass> DataList = new ArrayList<>();



    protected static Boolean VerifyTransaction(int SenderID,int ReceiverID, double Amount) throws SQLException {
        return SenderID == -1 || ReceiverID == -1 || DBFuncs.getUserBalence(SenderID) - Amount < 0 ? false : true;// Verifying if sender has enough balence
    }

    public static void Send(String Sender, String Receiver, String Amount_in_String) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, SQLException{
        double Amount = Double.parseDouble(Amount_in_String);
        int SenderID = DBFuncs.getUserId(Sender);
        int ReceiverID = DBFuncs.getUserId(Receiver);
        if (VerifyTransaction(SenderID, ReceiverID, Amount)) {
            DBFuncs.UpdateBalence(SenderID, -Amount);
            DBFuncs.UpdateBalence(ReceiverID, Amount);
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