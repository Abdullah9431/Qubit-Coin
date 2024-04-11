package blockchain;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import static blockchain.App.BlocktoVerify;
import static blockchain.App.BlockChain;


public class Mining extends Thread{

    public static void StartMining() throws NoSuchAlgorithmException {
        System.out.println("Mining started...");
        System.out.println("Verified block size: " + BlockChain.size());
        byte [] HashedPrevBlock = null;
        try {
            Block PrevBlock = BlockChain.get(BlockChain.size() - 1); 
            HashedPrevBlock = getHash(PrevBlock.content);
        } catch (Exception e) {}

        // get the block to verify from BlocktoVerify list 
        Block CurrentBlock = BlocktoVerify.remove(0);
        CurrentBlock.prevHash = HashedPrevBlock;
        SecureRandom random = new SecureRandom();
        do {
            byte[] HashedCurrBlock = getHash(CurrentBlock.content);
            byte[] to = ByteBuffer.allocate(4).putInt(random.nextInt(1000000)).array();
            if(Arrays.copyOfRange(HashedCurrBlock, 0, to.length) == to) break;
            CurrentBlock.updateNonce();
        } while (true);

        BlockChain.add(CurrentBlock);
        System.out.println("Block mined and added to Blockchain " + CurrentBlock.BlockId);
    }
    private static byte [] getHash(String content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestbytes = digest.digest(content.getBytes());
        return digestbytes; 
    }
}