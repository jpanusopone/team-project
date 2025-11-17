package view;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private final String viewName = "Login:";
    public static void main(String[] args) {
        JFrame frame = new  JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel itPanel = new JPanel();
        itPanel.setLayout(new GridLayout(2, 2));
        itPanel.add(new JLabel("Username:"));
        JTextField username = new JTextField(15);
        itPanel.add(username);
        itPanel.add(new JLabel("Password:"));
        JPasswordField password = new JPasswordField(15);
        itPanel.add(password);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Login"));
        buttonPanel.add(new JButton("Cancel"));

        mainPanel.add(itPanel);
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    public String getViewName() { return viewName;}
}
