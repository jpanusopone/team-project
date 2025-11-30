package use_case.explain_phishing;

public interface ExplainPhishingOutputBoundary {
    void presentSuccess(ExplainPhishingOutputData outputData);
    void presentFailure(String errorMessage);
}
