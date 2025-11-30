package interface_adapter.explain_phishing;

import entity.PhishingExplanation;

/**
 * ExplainPhishing State.
 *
 * Responsibility: Hold the current state of the ExplainPhishing view.
 *
 * Clean Architecture Layer: Interface Adapter Layer
 */
public class ExplainPhishingState {
    private PhishingExplanation explanation;
    private String errorMessage;

    public ExplainPhishingState() {
        this.explanation = null;
        this.errorMessage = null;
    }

    public ExplainPhishingState(PhishingExplanation explanation) {
        this.explanation = explanation;
        this.errorMessage = null;
    }

    public ExplainPhishingState(String errorMessage) {
        this.explanation = null;
        this.errorMessage = errorMessage;
    }

    public PhishingExplanation getExplanation() {
        return explanation;
    }

    public void setExplanation(PhishingExplanation explanation) {
        this.explanation = explanation;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null;
    }
}
