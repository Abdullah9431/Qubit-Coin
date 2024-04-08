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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class DBFuncs{
    
    public static Dataclass register(String Name) throws IOException, NoSuchAlgorithmException{
            Utilsinterface initialize = () -> {if (!Files.exists(Paths.get("src/DatabasePrivate.txt"))) Files.createFile(Paths.get("src/DatabasePrivate.txt"));};   
            initialize.lambda();

            int Id = (int) (Math.random() * 1000);
            //GENERATE A PAIR OF PUBLIC AND PRIVATE KEYS
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey UserPublicKey = keyPair.getPublic();
            PrivateKey UserPrivateKey = keyPair.getPrivate();
            //WRITE THE USER DATA TO THE DATABASE
            FileWriter writer = new FileWriter("src/Database.txt", true);
            writer.write(Id + " " + Name.replace(" ", "+") + " 1000" + "\n");
            writer.close();
            return new Dataclass(Id, UserPublicKey, UserPrivateKey);
        }

    public static void register (int a, String Name) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement = connection.createStatement();
            
            statement.executeUpdate("INSERT INTO USERS (USER_NAME) VALUES ('" + Name + "')");
            
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static int getUserId(int a, String word) {
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate("USE BLOCKCHAIN");
            ResultSet resultSet = statement1.executeQuery("SELECT USER_ID FROM USERS WHERE USER_NAME = '" + word + "'");
            if (resultSet.next()) return resultSet.getInt("USER_ID");
            System.out.println("got user id for" + " " + word);
            resultSet.close();
            statement1.close();
            statement2.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error in getting user id");
        }
        return -1;
    }

    public static int getUserBalence(int Id) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/Database.txt"));
        for (int i = lines.size() - 1; i >= 0; i--) {
            String[] parts = lines.get(i).split(" ");
            if (parts[0].equals(String.valueOf(Id))) return Integer.parseInt(parts[2]);  
        };
        return -1;
    }

}