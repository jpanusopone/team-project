package use_case.submit_email;

/**
 * Input boundary for the Submit Email use case.
 * Defines the method the controller calls to trigger the use case.
 */
public interface SubmitEmailInputBoundary {

    /**
     * Execute the submit email use case.
     *
     * @param inputData the raw email input data
     */
    void execute(SubmitEmailInputData inputData);
}
