package qubit;

// I dont know what peerinfo will be as string. however i have to set all peer info in a list
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class user1 {
    private String bootstrapIP;
    private int bootstrapPort;
    private int localPort;
    private List<InetSocketAddress> peers = new ArrayList<>();

    public user1(String bootstrapIP, int bootstrapPort, int localPort) {
        this.bootstrapIP = bootstrapIP;
        this.bootstrapPort = bootstrapPort;
        this.localPort = localPort;
    }

    public void start() {
        connectToBootstrapNode();
        startListening();
    }

    private void connectToBootstrapNode() {
        try (Socket socket = new Socket(bootstrapIP, bootstrapPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("Node user1: is listening on port " + localPort); // Send local port to bootstrap node

            // Receive list of peers from the bootstrap node
            String peerInfo = in.readLine();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListening() {
            try (ServerSocket serverSocket = new ServerSocket(localPort)) {
                System.out.println("Peer node is listening on port " + localPort);
                
                while (true) {
                    Socket socket = serverSocket.accept();
                    new HandleSocket(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private class HandleSocket extends Thread {
        private Socket socket;

        public HandleSocket(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println("Connection confirmed from peer : " + socket.getRemoteSocketAddress());
                out.println("Hello from " + socket.getLocalSocketAddress());
                
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

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java user1 <bootstrap_ip> <bootstrap_port> <local_port>");
            return;
        }

        String bootstrapIP = args[0];
        int bootstrapPort = Integer.parseInt(args[1]);
        int localPort = Integer.parseInt(args[2]);

        user1 node = new user1(bootstrapIP, bootstrapPort, localPort);
        node.start();
    }
}