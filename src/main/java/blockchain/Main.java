package blockchain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;



public class Main {
    public static void main(final String[] args) throws InterruptedException, IOException {
        File file = new File("src/Database.txt");
        if (file.exists()) {file.delete(); file.createNewFile();}
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            App.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}