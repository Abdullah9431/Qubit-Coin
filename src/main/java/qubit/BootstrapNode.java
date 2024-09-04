package qubit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BootstrapNode {
    private static final int PORT = 8000;
    private static List<InetSocketAddress> peers = new ArrayList<>();

    public static void Start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Bootstrap node is running on port " + PORT);
            
            while (true) {
                Socket socket = serverSocket.accept();
                new PeerHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class PeerHandler extends Thread {
        private Socket socket;

        public PeerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String peerAddress = socket.getInetAddress().getHostAddress();
                int peerPort = Integer.parseInt(in.readLine());
                
                // print the sent message
                System.out.println(in.readLine());

                // Add new peer to the list
                InetSocketAddress peer = new InetSocketAddress(peerAddress, peerPort);
                if (!peers.contains(peer)) {
                    peers.add(peer);
                    System.out.println("New peer connected: " + peer);
                }

                // make a copy of peers to send to the new peer
                List<InetSocketAddress> userView = Collections.unmodifiableList(new ArrayList<>(peers));
                out.println(userView);

                out.println("END");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}