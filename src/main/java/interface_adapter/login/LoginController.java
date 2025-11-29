package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import view.LoginView;

import javax.swing.*;

public class LoginController {

    private final LoginView loginView;
    private final ViewManagerModel viewManagerModel;

    public LoginController(LoginView loginView, ViewManagerModel viewManagerModel) {
        this.loginView = loginView;
        this.viewManagerModel = viewManagerModel;

        System.out.println("LoginController initialized");

        // Wire buttons:
        this.loginView.addLoginListener(e -> onLogin());
        this.loginView.addCancelListener(e -> onCancel());

        System.out.println("Login button listeners attached");
    }

    private void onLogin() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword());

        System.out.println("Login attempt - Username: '" + username + "', Password length: " + password.length());

        // Simple authentication (in production, this should use Firebase Auth or similar)
        boolean ok = username.equals("username") && password.equals("password");

        if (ok) {
            System.out.println("Login successful - switching to IT dashboard");
            // On success -> go to IT dashboard
            viewManagerModel.setState("itdashboard");
            viewManagerModel.firePropertyChange();
        } else {
            System.out.println("Login failed - invalid credentials");
            // Show an error
            JOptionPane.showMessageDialog(loginView,
                    "Username and password are wrong.",
                    "Login failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        // Back to start screen
        viewManagerModel.setState("start");
        viewManagerModel.firePropertyChange();
    }
}
