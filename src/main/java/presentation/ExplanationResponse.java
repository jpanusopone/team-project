package presentation;

import entity.PhishingExplanation;

public class ExplanationResponse {
    private final boolean success;
    private final PhishingExplanation explanation;
    private final String errorMessage;

    public ExplanationResponse(boolean success, PhishingExplanation explanation, String errorMessage) {
        this.success = success;
        this.explanation = explanation;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public PhishingExplanation getExplanation() { return explanation; }
    public String getErrorMessage() { return errorMessage; }
}
