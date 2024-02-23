package blockchain;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

class Register{
    
    public static Dataclass register(String Name) throws IOException, NoSuchAlgorithmException{
            Utilsinterface test = () -> {if (!Files.exists(Paths.get("src/DatabasePrivate.txt"))) Files.createFile(Paths.get("src/DatabasePrivate.txt"));};   
            test.lambda();

            int Id = (int) (Math.random() * 1000);
            //GENERATE A PAIR OF PUBLIC AND PRIVATE KEYS
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey UserPublicKey = keyPair.getPublic();
            PrivateKey UserPrivateKey = keyPair.getPrivate();
            //WRITE THE USER DATA TO THE DATABASE
            FileWriter writer = new FileWriter("src/Database.txt", true);
            writer.write(Id + " " + Name.replace(" ", "+") + " " + ((RSAPublicKey) UserPublicKey).getModulus() + " " + ((RSAPublicKey) UserPublicKey).getPublicExponent() + " " + ((RSAPrivateKey) UserPrivateKey).getModulus() + " " + ((RSAPrivateKey) UserPrivateKey).getPrivateExponent() + "\n");
            writer.close();
            return new Dataclass(Id, UserPublicKey, UserPrivateKey);
        }


    public static int getUserId(String word) throws IOException {
        List<String> lines= Files.readAllLines(Paths.get("src/Database.txt"));
        word = word.replace(" ", "+");
        for (int i = lines.size() - 1; i >= 0; i--) {
            String[] parts = lines.get(i).split(" ");
            if (parts[1].equals(word)) return Integer.parseInt(parts[0]);  
        };
        return -1;
    }

    // public static String getContentFromDatabase(int SenderId, String Key) throws IOException {
    //     int GetIndex = -1;
    //     if (Key.equals("PUBLIC KEY")) {
    //         GetIndex = 3;
    //     } else if (Key.equals("PRIVATE KEY")) {
    //         GetIndex = 4;
    //     }
    //     List<String> lines = Files.readAllLines(Paths.get("src/Database.txt"));
    //     for (String line : lines) {
    //         String[] parts = line.split(" ");
    //         if (Integer.parseInt(parts[0]) == SenderId) {
    //             return parts[GetIndex];
    //         }
    //     }
    //     return null;
    // }
}