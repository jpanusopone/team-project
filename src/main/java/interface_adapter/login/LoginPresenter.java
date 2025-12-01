package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * Presenter for the login use case.
 * Updates the ViewManagerModel based on login results.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final ViewManagerModel viewManagerModel;

    /**
     * Construct a LoginPresenter.
     *
     * @param viewManagerModel the view manager model that controls the active view
     */
    public LoginPresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        System.out.println("LoginPresenter: success for " + outputData.getUsername());
        viewManagerModel.setState("itdashboard");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // In a full clean-architecture setup, this would update a ViewModel,
        // and the view would observe and display the error.
        System.out.println("LoginPresenter: fail - " + errorMessage);
    }
}
