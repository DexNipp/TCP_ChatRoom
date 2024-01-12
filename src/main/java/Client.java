
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private boolean done;
    private ChatRoom chatRoom;

    @Override
    public void run() {
        try {
            client = new Socket("127.0.0.1", 5001);
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            String inputMessage;
            while ((inputMessage = input.readLine()) != null) {
                System.out.println(inputMessage);
                chatRoom.receiveMessage(inputMessage);
            }
        } catch (IOException ex) {
            shutdown();
        }
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public void shutdown() {
        done = true;
        try {
            input.close();
            output.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException ignore) {

        }
    }

    class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                while (!done) {
                    System.out.flush();
                    if (!chatRoom.message.isEmpty()) {
                        String message = chatRoom.message;
                        if (message.equals("/quit")) {
                            shutdown();
                        } else {
                            output.println(message); // Send message to the server
                        }
                    }
                    chatRoom.message = "";
                }
            } catch (Exception e) {
                shutdown();
            }
        }
    }
    public static void main(String[] args) {
        Client client = new Client();
        ChatRoom chatRoom = new ChatRoom();
        client.setChatRoom(chatRoom);
        chatRoom.startGUI();
        client.run();
    }
}