package blockchain;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

class Utils {

    protected static boolean VerifyTransaction(byte [] signatureBytes, String Msg, PublicKey UserPublicKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(UserPublicKey);
            signature.update(Msg.getBytes());
            if (signature.verify(signatureBytes)) {
                return true;
            } else {
                System.out.println("Transaction Not Verified");
                return false;
            }
            //CHECK IF THE SENDER HAS ENOUGH BALANCE
        }

        protected static byte[] Sign(String Msg, PrivateKey UserPrivateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        //SIGN THE TRANSACTION
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(UserPrivateKey);
        signature.update(Msg.getBytes("UTF8"));
        byte[] signatureBytes = signature.sign();
        return signatureBytes;
    }
}