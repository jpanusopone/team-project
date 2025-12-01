// ExplanationService.java (Interface Segregation Principle)

package use_case.interfaces;

import entity.PhishingExplanation;

/**
 * Service interface for generating phishing explanations.
 * Implementations provide different strategies for analyzing
 * email content and producing a {@link PhishingExplanation}.
 */
public interface ExplanationService {

    /**
     * Generates an explanation for the given email content.
     *
     * @param emailContent the raw email text to analyze
     * @return a structured phishing explanation result
     * @throws ExplanationException if the explanation cannot be generated
     */
    PhishingExplanation explainEmail(String emailContent) throws ExplanationException;
}
