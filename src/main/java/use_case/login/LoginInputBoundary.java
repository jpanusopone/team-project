package use_case.login;

/**
 * Input boundary for the login use case.
 * Implementations handle executing the login process
 * with the provided {@link LoginInputData}.
 */
public interface LoginInputBoundary {

    /**
     * Executes the login use case.
     *
     * @param inputData the data required to attempt login
     */
    void execute(LoginInputData inputData);
}
