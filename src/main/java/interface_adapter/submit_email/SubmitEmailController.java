package interface_adapter.submit_email;

import use_case.submit_email.SubmitEmailInputBoundary;
import use_case.submit_email.SubmitEmailInputData;

public class SubmitEmailController {

    private final SubmitEmailInputBoundary interactor;

    public SubmitEmailController(SubmitEmailInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void analyze(String rawEmail) {
        SubmitEmailInputData data = new SubmitEmailInputData(rawEmail);
        interactor.execute(data);
    }
}