package presentation;

import entity.PhishingExplanation;
import use_case.explain_phishing.ExplainPhishingInputBoundary;
import use_case.explain_phishing.ExplainPhishingInputData;
import use_case.explain_phishing.ExplainPhishingOutputData;

public class ExplanationController {
    private final ExplainPhishingInputBoundary explainPhishingInteractor;

    public ExplanationController(ExplainPhishingInputBoundary explainPhishingInteractor) {
        this.explainPhishingInteractor = explainPhishingInteractor;
    }

    public ExplanationResponse getExplanation(String emailContent) {
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);
        ExplainPhishingOutputData outputData = explainPhishingInteractor.execute(inputData);

        if (outputData.isSuccess()) {
            PhishingExplanation explanation = outputData.getExplanation();
            return new ExplanationResponse(true, explanation, null);
        } else {
            return new ExplanationResponse(false, null, outputData.getErrorMessage());
        }
    }
}