import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class it_signin {
    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        final JPanel itPanel = new JPanel();
        itPanel.setLayout(new GridLayout(2, 2));
        itPanel.add(new JLabel("Username:"));
        final JTextField username = new JTextField(15);
        itPanel.add(username);
        itPanel.add(new JLabel("Password:"));
        final JPasswordField password = new JPasswordField(15);
        itPanel.add(password);
        final JPanel buttonPanel = new JPanel();
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

}
