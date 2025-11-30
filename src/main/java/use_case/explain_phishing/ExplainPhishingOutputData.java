package use_case.explain_phishing;

import entity.PhishingExplanation;

public class ExplainPhishingOutputData {
    private final PhishingExplanation explanation;
    private final boolean success;
    private final String errorMessage;

    // Success constructor
    public ExplainPhishingOutputData(PhishingExplanation explanation) {
        this.explanation = explanation;
        this.success = true;
        this.errorMessage = null;
    }

    // Failure constructor
    public ExplainPhishingOutputData(String errorMessage) {
        this.explanation = null;
        this.success = false;
        this.errorMessage = errorMessage;
    }

    public PhishingExplanation getExplanation() {
        return explanation;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
