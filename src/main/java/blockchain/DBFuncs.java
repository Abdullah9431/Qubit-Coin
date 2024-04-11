package blockchain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBFuncs{

    public static int getUserId(String word) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE BLOCKCHAIN");
            ResultSet resultSet = statement.executeQuery("SELECT USER_ID FROM USERS WHERE USER_NAME = '" + word + "'");
            if (!resultSet.next()) {
                // statement.executeUpdate("USE BLOCKCHAIN");
                // resultSet = statement.executeQuery("SELECT BALANCE FROM USERS WHERE USER_ID = '"+ word +"'");
                // System.out.println("Failed to get resultSet, Trying again!!");
                return -1;
                }
            int returnValue = resultSet.getInt("USER_ID");
            resultSet.close();
            statement.close();
            connection.close();
            return returnValue;
            
    }

    public static void register (String Name) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO USERS (USER_NAME) VALUES ('" + Name + "')");
            System.out.println("User" + Name + " Registered Successfully");
            statement.close();
            connection.close();
    }

    public static int getUserBalence(int ID) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT BALANCE FROM USERS WHERE USER_ID = '"+ ID +"'");
            while (!resultSet.next()) {
                statement.executeUpdate("USE BLOCKCHAIN");
                resultSet = statement.executeQuery("SELECT BALANCE FROM USERS WHERE USER_ID = '"+ ID +"'");
                System.out.println("Failed to get resultSet, Trying again!!");
                }
            int returnValue = resultSet.getInt("BALANCE");
            resultSet.close();
            statement.close();
            connection.close();
            return returnValue;
    }

    protected static void UpdateBalence(int ID, double Amount) throws SQLException {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE USERS SET BALANCE = BALANCE + " + Amount + " WHERE USER_ID = " + ID);
            statement.close();
            connection.close();
    }

}
    
    // public static Dataclass register(String Name) throws IOException, NoSuchAlgorithmException{
    //         Utilsinterface initialize = () -> {if (!Files.exists(Paths.get("src/DatabasePrivate.txt"))) Files.createFile(Paths.get("src/DatabasePrivate.txt"));};   
    //         initialize.lambda();

    //         int Id = (int) (Math.random() * 1000);
    //         //GENERATE A PAIR OF PUBLIC AND PRIVATE KEYS
    //         KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    //         keyPairGenerator.initialize(2048);
    //         KeyPair keyPair = keyPairGenerator.generateKeyPair();
    //         PublicKey UserPublicKey = keyPair.getPublic();
    //         PrivateKey UserPrivateKey = keyPair.getPrivate();
    //         //WRITE THE USER DATA TO THE DATABASE
    //         FileWriter writer = new FileWriter("src/Database.txt", true);
    //         writer.write(Id + " " + Name.replace(" ", "+") + " 1000" + "\n");
    //         writer.close();
    //         return new Dataclass(Id, UserPublicKey, UserPrivateKey);
    //     }

    

    // public static int getUserId(String word) throws IOException {
    //     List<String> lines= Files.readAllLines(Paths.get("src/Database.txt"));
    //     word = word.replace(" ", "+");
    //     for (int i = lines.size() - 1; i >= 0; i--) {
    //         String[] parts = lines.get(i).split(" ");
    //         if (parts[1].equals(word)) return Integer.parseInt(parts[0]);  
    //     };
    //     return -1;
    // }