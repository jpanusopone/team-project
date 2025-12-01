package use_case;

import java.util.List;

import entity.PhishingExplanation;
import use_case.interfaces.ExplanationException;
import use_case.interfaces.ExplanationService;

/**
 * Use case that coordinates one or more explanation services to analyse a
 * phishing email and produce a structured {@link PhishingExplanation}.
 * Demonstrates Single Responsibility and Dependency Inversion principles.
 */
public class ExplainPhishingEmailUseCase {

    private final List<ExplanationService> explanationServices;

    /**
     * Construct the use case with a list of explanation services in priority order.
     *
     * @param explanationServices services used to analyse emails
     */
    public ExplainPhishingEmailUseCase(List<ExplanationService> explanationServices) {
        this.explanationServices = explanationServices;
    }

    /**
     * Execute the use case for the given email content.
     *
     * @param emailContent raw email body to analyse
     * @return phishing explanation produced by the first successful service
     * @throws ExplanationException if the content is empty or all services fail
     */
    public PhishingExplanation execute(String emailContent) throws ExplanationException {
        if (emailContent == null || emailContent.trim().isEmpty()) {
            throw new ExplanationException("Email content cannot be empty");
        }

        // Try each service in order (fallback pattern)
        for (ExplanationService service : explanationServices) {
            try {
                return service.explainEmail(emailContent);
            }
            catch (ExplanationException ex) {
                // Log and try next service
                System.err.println("Service failed: " + ex.getMessage());
            }
        }

        throw new ExplanationException("All explanation services failed");
    }
}
