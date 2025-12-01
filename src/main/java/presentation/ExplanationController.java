package presentation;

import entity.PhishingExplanation;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;

/**
 * Controller that coordinates phishing explanation requests.
 * Delegates to the ExplainPhishingEmailUseCase and wraps the result
 * in an {@link ExplanationResponse}.
 */
public class ExplanationController {

    private final ExplainPhishingEmailUseCase useCase;

    /**
     * Constructs an ExplanationController with the given use case.
     *
     * @param useCase the use case that explains phishing emails
     */
    public ExplanationController(ExplainPhishingEmailUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Gets a phishing explanation for the given email content.
     *
     * @param emailContent the raw email text to analyze
     * @return an ExplanationResponse indicating success or failure
     */
    public ExplanationResponse getExplanation(String emailContent) {
        ExplanationResponse response;
        try {
            final PhishingExplanation explanation = useCase.execute(emailContent);
            response = new ExplanationResponse(true, explanation, null);
        }
        catch (ExplanationException ex) {
            response = new ExplanationResponse(false, null, ex.getMessage());
        }
        return response;
    }
}
