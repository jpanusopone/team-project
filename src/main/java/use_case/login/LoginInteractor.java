package use_case.login;

public class LoginInteractor implements LoginInputBoundary {

    private final LoginOutputBoundary presenter;

    // You could also inject a UserDataAccessInterface here instead of hardcoding
    public LoginInteractor(LoginOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        String username = inputData.getUsername();
        String password = inputData.getPassword();

        System.out.println("LoginInteractor: username='" + username + "', password length=" + password.length());

        // Simple authentication logic for now
        boolean ok = "username".equals(username) && "password".equals(password);

        if (ok) {
            LoginOutputData outputData = new LoginOutputData(username);
            presenter.prepareSuccessView(outputData);
        } else {
            presenter.prepareFailView("Username and password are wrong.");
        }
    }
}
