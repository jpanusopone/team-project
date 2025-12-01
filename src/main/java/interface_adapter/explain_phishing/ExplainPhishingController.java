package interface_adapter.explain_phishing;

import use_case.explain_phishing.ExplainPhishingInputBoundary;
import use_case.explain_phishing.ExplainPhishingInputData;
import use_case.explain_phishing.ExplainPhishingOutputData;

/**
 * ExplainPhishing Controller.
 * Responsibility: Convert user input into use case input data and execute the use case.
 * Clean Architecture Layer: Interface Adapter Layer
 * Dependencies: Depends on use case (inward dependency)
 */
public class ExplainPhishingController {

    private final ExplainPhishingInputBoundary interactor;

    public ExplainPhishingController(ExplainPhishingInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Execute the ExplainPhishing use case.
     *
     * @param emailContent The raw email content to analyze
     * @return The output data containing the phishing explanation
     */
    public ExplainPhishingOutputData execute(String emailContent) {
        final ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);
        return interactor.execute(inputData);
    }
}
