package qubit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DBFuncs{
    final static String directoryPath = "/home/abdullah9431/WorkSpace/Qubit-Coin/";
    final static String dbUrl = "jdbc:mysql://localhost:3306/BLOCKCHAIN";
    final static String PublicKeyDirectory = "/home/abdullah9431/WorkSpace/Qubit-Coin/public_keys/";
    final static String PrivateKeyDirectory = "/home/abdullah9431/private_keys/";
    final static String User = "root";
    private final static String Pass = "abdullah9431";

    // returns the user id. If unregistered found, returns -1
    protected static int getUserId(String word) throws SQLException {
            Connection connection = DriverManager.getConnection(dbUrl, User, Pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT USER_ID FROM USERS WHERE USER_NAME = '" + word + "'");
            resultSet.next();
            int returnValue;
            if (resultSet.next()) {
                returnValue = resultSet.getInt("USER_ID");
            } else {
                returnValue = -1;
                System.out.println("User " + word + " not found!");
            }
            resultSet.close();
            statement.close();
            connection.close();
            return returnValue;
            
    }

    public static void register (String Name) throws SQLException, NoSuchAlgorithmException, IOException {
            int Id = (int) (Math.random() * 10000);
            //GENERATE A PAIR OF PUBLIC AND PRIVATE KEYS
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey UserPublicKey = keyPair.getPublic();
            PrivateKey UserPrivateKey = keyPair.getPrivate();

            // Store the private key in the specified folder
            storeKey(UserPublicKey, PublicKeyDirectory, "public" + String.valueOf(Id) + ".key");
            storeKey(UserPrivateKey, PrivateKeyDirectory, "private" + String.valueOf(Id) + ".key");            
            Main.logger.debug("stored key in directories");

            // Add user to mysql database
            Connection connection = DriverManager.getConnection(dbUrl, User, "abdullah9431");
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO USERS (USER_ID,USER_NAME,BALANCE) VALUES ('" + Id + "', '" + Name + "', '" + String.valueOf(10000) + "')");
            System.out.println("User  " + Name + " Registered Successfully to db");
            statement.close();
            connection.close();
    }

    public static int getUserBalence(int ID) throws SQLException {
            Connection connection = DriverManager.getConnection(dbUrl, User, Pass);
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
            Connection connection = DriverManager.getConnection(dbUrl, User, Pass);
            Statement statement = connection.createStatement();
            statement.executeUpdate("UPDATE USERS SET BALANCE = BALANCE + " + Amount + " WHERE USER_ID = " + ID);
            statement.close();
            connection.close();
    }

    public static void storeKey(PrivateKey privateKey, String directoryPath, String fileName) throws IOException {
        // Ensure the directory exists
        Main.logger.debug("started key storing in dir" + PrivateKeyDirectory);
        File directory = new File(PrivateKeyDirectory);
        if (!directory.exists()) {
            Main.logger.debug("Directory does not exist, attempting to create it.");
            if (!directory.mkdirs()) {
                Main.logger.debug("Failed to create directory: " + PrivateKeyDirectory);
                throw new IOException("Cannot create directory: " + PrivateKeyDirectory);
            }
        }

        // Write the private key to the file
        Main.logger.debug("writing key");
        FileOutputStream fos = new FileOutputStream(new File(PrivateKeyDirectory, fileName));
        fos.write(privateKey.getEncoded());
        fos.close();

        // Set file permissions to be readable only by the owner (Unix-based systems)
        setFilePermissions(PrivateKeyDirectory + File.separator + fileName);
    }

    public static void storeKey(PublicKey publicKey, String directoryPath, String fileName) throws IOException {
        // Ensure the directory exists
        File directory = new File(PublicKeyDirectory);
        if (!directory.exists()) {
            Main.logger.debug("Directory does not exist, attempting to create it.");
            if (!directory.mkdirs()) {
                Main.logger.debug("Failed to create directory: " + PublicKeyDirectory);
                throw new IOException("Cannot create directory: " + PublicKeyDirectory);
            }
        }

        if (!directory.canWrite()) {
            Main.logger.error("Cannot write to directory: " + PublicKeyDirectory);
            throw new IOException("Cannot write to directory: " + PublicKeyDirectory);
    }

        // Write the private key to the file
        try (FileOutputStream fos = new FileOutputStream(new File(PublicKeyDirectory, fileName))) {
            fos.write(publicKey.getEncoded());
        } catch (IOException e) {
            Main.logger.error("Failed to write public key to file.", e);
            throw e;
        }

        // Set file permissions to be readable only by the owner (Unix-based systems)
        setFilePermissions(PublicKeyDirectory + File.separator + fileName);
    }

    public static PublicKey getPublicKey(int Id) throws Exception {
        // Read the public key from the file
        String filename = PublicKeyDirectory + "public" + String.valueOf(Id) + ".key";
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] keyBytes = new byte[(int) file.length()];
        fis.read(keyBytes);
        fis.close();

        // Convert the byte array into a PublicKey
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // Change to appropriate algorithm if needed
        return keyFactory.generatePublic(spec);
    }

    public static PrivateKey getPrivateKey(int Id) throws Exception {
        // Read the private key from the file
        String filename = PrivateKeyDirectory + "private" + String.valueOf(Id) + ".key";
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] keyBytes = new byte[(int) file.length()];
        fis.read(keyBytes);
        fis.close();

        // Convert the byte array into a PrivateKey
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }
    public static void setFilePermissions(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            // Set file permissions to read-only for the owner
            file.setReadable(false, false); // No one can read
            file.setReadable(true, true); // Only owner can read
            file.setWritable(false, false); // No one can write
            file.setWritable(true, true); // Only owner can write
            file.setExecutable(false, false); // No one can execute
        }
        }
}