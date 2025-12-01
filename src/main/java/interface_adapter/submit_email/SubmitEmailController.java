package interface_adapter.submit_email;

import use_case.submit_email.SubmitEmailInputBoundary;
import use_case.submit_email.SubmitEmailInputData;

/**
 * Controller for submitting and analyzing raw email text.
 * Converts user input into InputData and passes it to the interactor.
 */
public class SubmitEmailController {

    private final SubmitEmailInputBoundary interactor;

    /**
     * Creates a controller with the associated interactor.
     *
     * @param interactor the submit email use case boundary
     */
    public SubmitEmailController(SubmitEmailInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Analyzes the provided raw email text by passing it to the interactor.
     *
     * @param rawEmail the unprocessed email text provided by the user
     */
    public void analyze(String rawEmail) {
        final SubmitEmailInputData data = new SubmitEmailInputData(rawEmail);
        interactor.execute(data);
    }
}
