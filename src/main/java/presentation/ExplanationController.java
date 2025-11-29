package presentation;

import entity.PhishingExplanation;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;

public class ExplanationController {
    private final ExplainPhishingEmailUseCase useCase;

    public ExplanationController(ExplainPhishingEmailUseCase useCase) {
        this.useCase = useCase;
    }

    public ExplanationResponse getExplanation(String emailContent) {
        try {
            PhishingExplanation explanation = useCase.execute(emailContent);
            return new ExplanationResponse(true, explanation, null);
        } catch (ExplanationException e) {
            return new ExplanationResponse(false, null, e.getMessage());
        }
    }
}