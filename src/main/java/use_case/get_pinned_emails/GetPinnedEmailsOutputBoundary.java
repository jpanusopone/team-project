package use_case.get_pinned_emails;

/**
 * The output boundary for the Get Pinned Emails Use Case.
 */
public interface GetPinnedEmailsOutputBoundary {
    /**
     * Prepares the success view for the Get Pinned Emails Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(GetPinnedEmailsOutputData outputData);

    /**
     * Prepares the failure view for the View Dashboard Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
