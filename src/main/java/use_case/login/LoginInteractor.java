package use_case.login;

/**
 * The interactor responsible for executing the login use case.
 */
public class LoginInteractor implements LoginInputBoundary {

    private final LoginOutputBoundary presenter;

    /**
     * Creates a new LoginInteractor.
     *
     * @param presenter the output boundary responsible for presenting results
     */
    public LoginInteractor(LoginOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(LoginInputData inputData) {
        final String username = inputData.getUsername();
        final String password = inputData.getPassword();

        System.out.println("LoginInteractor: username='" + username
                + "', password length=" + password.length());

        final boolean ok = "username".equals(username) && "password".equals(password);

        if (ok) {
            final LoginOutputData outputData = new LoginOutputData(username);
            presenter.prepareSuccessView(outputData);
        }
        else {
            presenter.prepareFailView("Username and password are wrong.");
        }
    }
}
