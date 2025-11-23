package use_case.submit_email;

import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import presentation.ExplanationController;
import presentation.ExplanationResponse;

public class SubmitEmailInteractor implements SubmitEmailInputBoundary {

    private final SubmitEmailOutputBoundary presenter;
    private final ExplanationController explanationController;

    public SubmitEmailInteractor(SubmitEmailOutputBoundary presenter,
                                 ExplanationController explanationController) {
        this.presenter = presenter;
        this.explanationController = explanationController;
    }

    private int mapRisk(RiskLevel level) {
        return switch (level) {
            case HIGH -> 100;
            case MEDIUM -> 50;
            case LOW -> 20;
        };
    }

    @Override
    public void execute(SubmitEmailInputData inputData) {
        String raw = inputData.getRawEmail();

        if (raw == null || raw.trim().isEmpty()) {
            SubmitEmailOutputData out = new SubmitEmailOutputData(
                    "", "", 0, "",
                    "Please paste an email before analyzing."
            );
            presenter.present(out);
            return;
        }

        try {
            ExplanationResponse response = explanationController.getExplanation(raw);

            if (!response.isSuccess()) {
                presenter.present(new SubmitEmailOutputData(
                        "", "", 0, "",
                        "Failed to analyze email: " + response.getErrorMessage()
                ));
                return;
            }

            PhishingExplanation expl = response.getExplanation();

            Email email = new EmailBuilder()
                    .body(raw)
                    .suspicionScore(mapRisk(expl.getRiskLevel()))
                    .explanation(String.join("\n", expl.getReasons()))
                    .links(expl.getIndicators().getUrls())
                    .build();


            SubmitEmailOutputData out = new SubmitEmailOutputData(
                    email.getTitle(),
                    email.getSender(),
                    (int) email.getSuspicionScore(),
                    email.getExplanation(),
                    null
            );
            presenter.present(out);

        } catch (IllegalStateException e) {
            presenter.present(new SubmitEmailOutputData(
                    "", "", 0, "",
                    "OPENAI_API_KEY is not set on this machine."
            ));
        } catch (Exception e) {
            presenter.present(new SubmitEmailOutputData(
                    "", "", 0, "",
                    "Unable to analyse this email. Please try again later."
            ));
        }
    }
}