package presentation;

import entity.PhishingExplanation;
import use_case.explain_phishing.ExplainPhishingInputBoundary;
import use_case.explain_phishing.ExplainPhishingInputData;
import use_case.explain_phishing.ExplainPhishingOutputData;

/**
 * Controller that coordinates phishing explanation requests.
 * Delegates to the explain-phishing use case and wraps the result
 * in an {@link ExplanationResponse}.
 */
public class ExplanationController {

    private final ExplainPhishingInputBoundary explainPhishingInteractor;

    /**
     * Constructs an ExplanationController with the given use case.
     *
     * @param explainPhishingInteractor the use case that explains phishing emails
     */
    public ExplanationController(ExplainPhishingInputBoundary explainPhishingInteractor) {
        this.explainPhishingInteractor = explainPhishingInteractor;
    }

    /**
     * Gets a phishing explanation for the given email content.
     *
     * @param emailContent the raw email text to analyze
     * @return an ExplanationResponse indicating success or failure
     */
    public ExplanationResponse getExplanation(String emailContent) {
        final ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);
        final ExplainPhishingOutputData outputData = explainPhishingInteractor.execute(inputData);

        final ExplanationResponse response;
        if (outputData.isSuccess()) {
            final PhishingExplanation explanation = outputData.getExplanation();
            response = new ExplanationResponse(true, explanation, null);
        }
        else {
            response = new ExplanationResponse(false, null, outputData.getErrorMessage());
        }
        return response;
    }
}
