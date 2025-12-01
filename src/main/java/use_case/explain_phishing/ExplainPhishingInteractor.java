package use_case.explain_phishing;

import entity.PhishingExplanation;
import use_case.interfaces.ExplanationService;
import use_case.interfaces.ExplanationException;

import java.util.List;

/**
 * ExplainPhishing Use Case Interactor.
 *
 * Responsibility: Orchestrate getting phishing explanations from multiple
 * explanation services (DeepSeek, OpenAI, etc.) using a fallback pattern.
 *
 * Clean Architecture Layer: Use Case Layer
 * Dependencies: Only depends on entities and use case interfaces
 */
public class ExplainPhishingInteractor implements ExplainPhishingInputBoundary {

    private final List<ExplanationService> explanationServices;

    public ExplainPhishingInteractor(List<ExplanationService> explanationServices) {
        this.explanationServices = explanationServices;
    }

    @Override
    public ExplainPhishingOutputData execute(ExplainPhishingInputData inputData) {
        String emailContent = inputData.getEmailContent();

        // Validate input
        if (emailContent == null || emailContent.trim().isEmpty()) {
            return new ExplainPhishingOutputData("Email content cannot be empty");
        }

        // Try each service in order (fallback pattern)
        for (ExplanationService service : explanationServices) {
            try {
                PhishingExplanation explanation = service.explainEmail(emailContent);
                return new ExplainPhishingOutputData(explanation);
            } catch (ExplanationException e) {
                // Log and try next service
                System.err.println("Service " + service.getClass().getSimpleName() +
                                 " failed: " + e.getMessage());
                // Continue to next service
            }
        }

        // All services failed
        return new ExplainPhishingOutputData("All explanation services failed");
    }
}
