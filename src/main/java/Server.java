import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ArrayList<ConnectionHandler> connections;
    private ServerSocket server;
    private boolean done;
    private ExecutorService threadPool;

//    private ChatRoom chatRoom;


    public Server() {
        connections = new ArrayList<>();
        done = false;
    }

    @Override
    public void run() {
        try {
            server = new ServerSocket(5001);
            threadPool = Executors.newCachedThreadPool();
            while (!done) {
                Socket client = server.accept();
                ConnectionHandler handler = new ConnectionHandler(client);
                connections.add(handler);
                threadPool.execute(handler);
            }

        } catch (Exception e) {
            shutdown();
        }
    }

    public void broadcast(String message) {
        for (ConnectionHandler ch : connections) {
            if (ch != null) {
                broadcast(message);
            }
        }
    }

    public void shutdown() {
        try {
            if (!server.isClosed()) {
                server.close();
            }
            for (ConnectionHandler ch : connections) {
                ch.shutdown();
            }
        } catch (IOException exception) {
            // ignore
        }
    }

//    public void setChatRoom(ChatRoom chatRoom) {
//        this.chatRoom = chatRoom;
//    }

    class ConnectionHandler implements Runnable {
        private Socket client;
        private BufferedReader input;
        private PrintWriter output;
        private String nickname;

        private ChatRoom chatRoom;

        public ConnectionHandler(Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            try {
                output = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                output.println("Enter a nickname: ");
                nickname = input.readLine();
                sendMessage(nickname + " has joined the chat.");

                String message;
                while ((message = input.readLine()) != null) {
                    if (message.startsWith("/nickname ")) {
                        String[] messageSplit = message.split(" ", 2);
                        if (messageSplit.length == 2) {
                            broadcast(nickname + " changed name to: " + messageSplit[1]);
                            nickname = messageSplit[1];
                        }
                    }
                    else if (message.startsWith("/quit")) {
                        broadcast(nickname + " has left the chat.");
                    } else {
                        broadcast(nickname + ": " + message);
                    }
                }

            } catch (IOException e) {
                shutdown();
            }

        }

        public void sendMessage(String message) {
            output.println(message);
        }

        public void shutdown() {
            try {
                input.close();
                output.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException ex) {
                //ignore
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
