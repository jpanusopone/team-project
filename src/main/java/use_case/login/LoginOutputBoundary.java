package use_case.login;

/**
 * Output boundary for the login use case.
 * Defines methods for presenting success or failure results.
 */
public interface LoginOutputBoundary {

    /**
     * Prepares the view for a successful login.
     *
     * @param outputData the data associated with the successful login
     */
    void prepareSuccessView(LoginOutputData outputData);

    /**
     * Prepares the view for a failed login attempt.
     *
     * @param errorMessage the error message describing the failure
     */
    void prepareFailView(String errorMessage);
}
