package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import view.LoginView;

import javax.swing.*;

public class loginController {

    private final LoginView loginView;
    private final ViewManagerModel viewManagerModel;
    
    public  loginController(LoginView loginView, ViewManagerModel viewManagerModel) {
        this.loginView = loginView;
        this.viewManagerModel = viewManagerModel;

        // Wire buttons:
        this.loginView.addLoginListener(e -> onLogin());
        this.loginView.addCancelListener(e -> onCancel());
    }

    private void onLogin() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword());

        boolean ok = username.equals("username") && password.equals("password");

        if (ok) {
            // On success -> go to IT dashboard
            viewManagerModel.setState("itdashboard");
            viewManagerModel.firePropertyChange();
        } else {
            // Show an error
            // Either via label:
            // loginView.setErrorMessage("Invalid username or password");
            // Or with a popup:
            JOptionPane.showMessageDialog(loginView,
                    "Invalid username or password",
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
