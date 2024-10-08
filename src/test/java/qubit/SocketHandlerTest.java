package qubit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

public class SocketHandlerTest {

    @Test
    public void testSendMessage() throws IOException {
        // Mocking the socket and output stream
        Socket mockSocket = mock(Socket.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        PrintWriter mockWriter = new PrintWriter(mockOutputStream, true);

        // Stubbing the methods
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        BootstrapNode node = new BootstrapNode();
        node.Start();
        

        // Simulating the sendMessage method
        SocketHandler handler = new SocketHandler(mockSocket);
        handler.sendMessage("Hello");

        // Verify that the correct methods were called
        verify(mockOutputStream).write(any(byte[].class), eq(0), anyInt());
    }
}

class SocketHandler {
    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);
    }
}