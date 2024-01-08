import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class Server implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(5001);
            Socket client = server.accept();
        } catch (IOException e) {
            // TODO: handle
        }
    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader input;
        private PrintWriter output;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            try {
                output = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                // TODO: handle
            }

        }
    }
}
