// ExplainPhishingEmailUseCase.java (Single Responsibility Principle)
package use_case;

import entity.PhishingExplanation;
import use_case.interfaces.ExplanationService;
import use_case.interfaces.ExplanationException;
import java.util.List;

public class ExplainPhishingEmailUseCase {
    private final List<ExplanationService> explanationServices;

    // Dependency Inversion Principle - depend on abstractions
    public ExplainPhishingEmailUseCase(List<ExplanationService> explanationServices) {
        this.explanationServices = explanationServices;
    }

    public PhishingExplanation execute(String emailContent) throws ExplanationException {
        if (emailContent == null || emailContent.trim().isEmpty()) {
            throw new ExplanationException("Email content cannot be empty");
        }

        // Try each service in order (fallback pattern)
        for (ExplanationService service : explanationServices) {
            try {
                return service.explainEmail(emailContent);
            } catch (ExplanationException e) {
                // Log and try next service
                System.err.println("Service failed: " + e.getMessage());
                continue;
            }
        }

        throw new ExplanationException("All explanation services failed");
    }
}