import java.io.*;
import java.net.*;

public class Client implements Runnable {
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private boolean done;
    @Override
    public void run() {
        try {
            Socket client = new Socket("127.0.0.1", 5001);
            output = new PrintWriter(client.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler inputHandler = new InputHandler();
            Thread thread = new Thread(inputHandler);
            thread.start();

            String inputMessage;
            while ((inputMessage = input.readLine()) != null) {
                System.out.println(inputMessage);
            }
        } catch (IOException ex) {
            shutdown();
        }
    }

    public void shutdown() {
        done = true;
        try {
            input.close();
            output.close();
            if (!client.isClosed()) {
                client.close();
            }
        } catch (IOException ex) {
            // ignore
        }
    }

    class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                while (!done) {
                    String message = inputReader.readLine();
                    if (message.equals("/quit")) {
                        inputReader.close();
                        shutdown();
                    } else {
                        output.println(message);
                    }
                }
            } catch (IOException ex) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
