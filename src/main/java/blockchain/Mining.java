package blockchain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Mining extends Thread{

    public static void StartMining() throws InterruptedException, NoSuchAlgorithmException {
        System.out.println("Mining started...");
        System.out.println("Verified block size: " + Main.VerifiedBlock.size());
        Block block = null;
        if (Main.VerifiedBlock.size() > 0) {
            block = Main.VerifiedBlock.get(Main.VerifiedBlock.size() - 1);
            block.prevHash = Main.prevhash;
            System.out.println("Block hash setted: " + block.prevHash.hashCode());
        }
        Block CurrentBlock = Main.BlocktoVerify.remove(0);
        Main.VerifiedBlock.add(CurrentBlock);
        String content = CurrentBlock.content;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        Main.prevhash = digest.digest(content.getBytes());
        if(CurrentBlock.BlockId > 1) {System.out.println("Blockid: " + CurrentBlock.BlockId);System.out.println(block.prevHash.hashCode());}
        System.out.println("Block mined: " + CurrentBlock.BlockId);
    }
}