


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatRoom extends JFrame {
    private JTextArea chatArea;
    public JTextField inputField;
    public String message = "";

    public void startGUI() {
        setTitle("ChatRoom");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centering

        initUI();
        setVisible(true);
    }
    public void initUI() {

        setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(25);
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> sendMessage());

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }
    public synchronized void sendMessage() {
        message = inputField.getText();
        if (!message.isEmpty()) {
            inputField.setText("");
        }
    }

    public void receiveMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}