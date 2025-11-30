package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
import view.LoginView;

import javax.swing.*;

public class LoginController {

    private final LoginView loginView;
    private final ViewManagerModel viewManagerModel;
    private final LoginInputBoundary loginInteractor;

    public LoginController(LoginView loginView,
                           ViewManagerModel viewManagerModel,
                           LoginInputBoundary loginInteractor) {
        this.loginView = loginView;
        this.viewManagerModel = viewManagerModel;
        this.loginInteractor = loginInteractor;

        System.out.println("LoginController initialized");

        this.loginView.addLoginListener(e -> onLogin());
        this.loginView.addCancelListener(e -> onCancel());

        System.out.println("Login button listeners attached");
    }

    private void onLogin() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword());

        LoginInputData inputData = new LoginInputData(username, password);
        loginInteractor.execute(inputData);
    }

    private void onCancel() {
        viewManagerModel.setState("start");
        viewManagerModel.firePropertyChange();
    }
}
