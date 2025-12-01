package use_case.save_email;

/**
 * Output boundary for the Save Email use case.
 * Defines how success and error results should be presented.
 */
public interface SaveEmailOutputBoundary {

    /**
     * Present a successful result of saving an email.
     *
     * @param outputData the data containing success information
     */
    void presentSuccess(SaveEmailOutputData outputData);

    /**
     * Present an error result of saving an email.
     *
     * @param outputData the data containing error information
     */
    void presentError(SaveEmailOutputData outputData);
}
