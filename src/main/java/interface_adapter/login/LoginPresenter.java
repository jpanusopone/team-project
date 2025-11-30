package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

import javax.swing.*;

public class LoginPresenter implements LoginOutputBoundary {

    private final ViewManagerModel viewManagerModel;

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
        System.out.println("LoginPresenter: fail - " + errorMessage);
        // In "proper" clean architecture this would go via a ViewModel,
        // but this keeps it close to your current behaviour:
        JOptionPane.showMessageDialog(null,
                errorMessage,
                "Login failed",
                JOptionPane.ERROR_MESSAGE);
    }
}
