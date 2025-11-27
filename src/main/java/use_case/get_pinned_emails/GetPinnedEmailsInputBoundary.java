package use_case.get_pinned_emails;

/**
 * Input Boundary for the View Dashboard Use Case.
 */
public interface GetPinnedEmailsInputBoundary {

    /**
     * Executes the View Dashboard Use Case.
     * @param getPinnedEmailsInputData the input data
     */
    void execute(GetPinnedEmailsInputData getPinnedEmailsInputData);
}
