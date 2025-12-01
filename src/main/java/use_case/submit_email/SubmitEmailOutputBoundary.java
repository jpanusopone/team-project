package use_case.submit_email;

/**
 * Output boundary for the Submit Email use case.
 * Responsible for presenting the results of email analysis.
 */
public interface SubmitEmailOutputBoundary {

    /**
     * Present the output data produced by the interactor.
     *
     * @param outputData the data to be displayed
     */
    void present(SubmitEmailOutputData outputData);
}
