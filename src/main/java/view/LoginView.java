package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.ActionListener;

public class LoginView extends JPanel {
    private final String viewName = "login";

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JButton loginButton = new JButton("Login");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton backToDashboardButton = new JButton("Back to Dashboard");

    public LoginView() {
        setLayout(new GridBagLayout()); // center everything
        setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 25, 10);

        // Title
        JLabel title = new JLabel("IT Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        add(title, gbc);

        // --- Username Label ---
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(new JLabel("Username:"), gbc);

        // --- Username Field ---
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // --- Password Label ---
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        // --- Password Field ---
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(buttonPanel, gbc);

        // --- Back to Dashboard Button ---
        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(backToDashboardButton, gbc);
    }

    // Optional getters so controllers can add listeners / read fields
    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JButton getBackToDashboardButton() {
        return backToDashboardButton;
    }

    public void addBackToDashboardListener(ActionListener listener) {
        backToDashboardButton.addActionListener(listener);
    }

    public String getViewName() { return viewName;}
}
