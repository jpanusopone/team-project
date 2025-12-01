package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
import view.LoginView;

/**
 * Controller for handling login logic.
 */
public class LoginController {

    private final LoginView loginView;
    private final ViewManagerModel viewManagerModel;
    private final LoginInputBoundary loginInteractor;

    /**
     * Construct a LoginController.
     *
     * @param loginView the login view
     * @param viewManagerModel the view manager model
     * @param loginInteractor the login interactor
     */
    public LoginController(LoginView loginView,
                           ViewManagerModel viewManagerModel,
                           LoginInputBoundary loginInteractor) {
        this.loginView = loginView;
        this.viewManagerModel = viewManagerModel;
        this.loginInteractor = loginInteractor;

        System.out.println("LoginController initialized");

        this.loginView.addLoginListener(idEvent -> onLogin());
        this.loginView.addCancelListener(idEvent -> onCancel());

        System.out.println("Login button listeners attached");
    }

    /**
     * Handle login button pressed.
     */
    private void onLogin() {
        final String username = loginView.getUsernameField().getText().trim();
        final String password = new String(loginView.getPasswordField().getPassword());

        final LoginInputData inputData = new LoginInputData(username, password);
        loginInteractor.execute(inputData);
    }

    /**
     * Handle cancel button pressed.
     */
    private void onCancel() {
        viewManagerModel.setState("start");
        viewManagerModel.firePropertyChange();
    }
}
