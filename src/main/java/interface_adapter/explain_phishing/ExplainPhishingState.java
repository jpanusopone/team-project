package interface_adapter.explain_phishing;

import entity.PhishingExplanation;

/**
 * ExplainPhishing State.
 *
 * <p>Responsibility: Hold the current state of the ExplainPhishing view.
 *
 * <p>Clean Architecture Layer: Interface Adapter Layer
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

    /**
     * Indicates whether this state contains an error.
     *
     * <p>This method returns {@code true} if an error message has been set
     * for the ExplainPhishing view, meaning the use case execution resulted
     * in a failure state rather than a successful explanation.
     *
     * @return {@code true} if an error message is present; {@code false} otherwise
     */
    public boolean hasError() {
        return errorMessage != null;
    }
}
