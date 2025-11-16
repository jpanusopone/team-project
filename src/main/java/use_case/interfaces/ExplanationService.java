// ExplanationService.java (Interface Segregation Principle)
package use_case.interfaces;

import entity.PhishingExplanation;

public interface ExplanationService {
    PhishingExplanation explainEmail(String emailContent) throws ExplanationException;
}