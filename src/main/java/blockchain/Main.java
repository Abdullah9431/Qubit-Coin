package blockchain;

import java.io.File;
import java.io.IOException;




public class Main {
    public static void main(final String[] args) throws InterruptedException, IOException {
        File file = new File("src/Database.txt");
        if (file.exists()) {file.delete(); file.createNewFile();}
        try {
            // Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BLOCKCHAIN", "root", "abdullah9431");
            // App.main(args);
            int SenderID = DBFuncs.getUserId(1, "ALI");
            System.out.println(SenderID);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}