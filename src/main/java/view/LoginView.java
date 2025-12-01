package view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * The login view for IT staff.
 * Provides username/password fields and navigation buttons.
 */
public class LoginView extends JPanel {

    private static final int BORDER_PADDING = 40;
    private static final int FIELD_COLUMNS = 20;
    private static final int INSET_SMALL = 10;
    private static final int INSET_TITLE_BOTTOM = 25;
    private static final int TITLE_FONT_SIZE = 26;
    private static final int BUTTON_TOP_INSET = 20;

    private final String viewName = "login";

    private final JTextField usernameField = new JTextField(FIELD_COLUMNS);
    private final JPasswordField passwordField = new JPasswordField(FIELD_COLUMNS);
    private final JButton loginButton = new JButton("Login");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton backToDashboardButton = new JButton("Back to Dashboard");

    /**
     * Constructs the login view with username/password inputs and controls.
     */
    public LoginView() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(BORDER_PADDING, BORDER_PADDING,
                BORDER_PADDING, BORDER_PADDING));

        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(INSET_SMALL, INSET_SMALL,
                INSET_TITLE_BOTTOM, INSET_SMALL);

        // Title
        final JLabel title = new JLabel("IT Login");
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        add(title, gbc);

        // --- Username Label ---
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(INSET_SMALL, INSET_SMALL,
                INSET_SMALL, INSET_SMALL);
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
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(BUTTON_TOP_INSET, INSET_SMALL,
                INSET_SMALL, INSET_SMALL);
        add(buttonPanel, gbc);

        // --- Back to Dashboard Button ---
        gbc.gridy++;
        gbc.insets = new Insets(INSET_SMALL, INSET_SMALL,
                INSET_SMALL, INSET_SMALL);
        add(backToDashboardButton, gbc);
    }

    /**
     * Returns the username text field.
     *
     * @return the username text field
     */
    public JTextField getUsernameField() {
        return usernameField;
    }

    /**
     * Returns the password field.
     *
     * @return the password field
     */
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    /**
     * Returns the login button.
     *
     * @return the login button
     */
    public JButton getLoginButton() {
        return loginButton;
    }

    /**
     * Returns the cancel button.
     *
     * @return the cancel button
     */
    public JButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Returns the "Back to Dashboard" button.
     *
     * @return the back-to-dashboard button
     */
    public JButton getBackToDashboardButton() {
        return backToDashboardButton;
    }

    /**
     * Adds a listener to the "Back to Dashboard" button.
     *
     * @param listener the action listener to add
     */
    public void addBackToDashboardListener(ActionListener listener) {
        backToDashboardButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the "Login" button.
     *
     * @param listener the action listener to add
     */
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the "Cancel" button.
     *
     * @param listener the action listener to add
     */
    public void addCancelListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    /**
     * Returns the logical name for this view used by the ViewManager.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }
}
