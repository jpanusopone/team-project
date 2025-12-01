package use_case.explain_phishing;

import java.util.List;

import entity.PhishingExplanation;
import use_case.interfaces.ExplanationException;
import use_case.interfaces.ExplanationService;

/**
 * ExplainPhishing Use Case Interactor.
 *
 * <p>Responsibility: Orchestrate getting phishing explanations from multiple
 * explanation services (DeepSeek, OpenAI, etc.) using a fallback pattern.
 *
 * <p>Clean Architecture Layer: Use Case Layer
 *
 * <p>Dependencies: Only depends on entities and use case interfaces
 */
public class ExplainPhishingInteractor implements ExplainPhishingInputBoundary {

    private final List<ExplanationService> explanationServices;

    public ExplainPhishingInteractor(List<ExplanationService> explanationServices) {
        this.explanationServices = explanationServices;
    }

    @Override
    public ExplainPhishingOutputData execute(ExplainPhishingInputData inputData) {
        final String emailContent = inputData.getEmailContent();

        // Validate input
        if (emailContent == null || emailContent.trim().isEmpty()) {
            return new ExplainPhishingOutputData("Email content cannot be empty");
        }

        // Try each service in order (fallback pattern)
        for (ExplanationService service : explanationServices) {
            try {
                final PhishingExplanation explanation = service.explainEmail(emailContent);
                return new ExplainPhishingOutputData(explanation);
            }
            catch (ExplanationException e) {
                // Log and try next service
                System.err.println("Service " + service.getClass().getSimpleName()
                        + " failed: " + e.getMessage());
                // Continue to next service
            }
        }

        // All services failed
        return new ExplainPhishingOutputData("All explanation services failed");
    }
}
