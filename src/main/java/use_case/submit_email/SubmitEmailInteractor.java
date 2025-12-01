package use_case.submit_email;

import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import use_case.explain_phishing.ExplainPhishingInputBoundary;
import use_case.explain_phishing.ExplainPhishingInputData;
import use_case.explain_phishing.ExplainPhishingOutputData;

/**
 * Interactor for the Submit Email use case.
 * Validates raw email input, calls the explanation use case,
 * maps the result, and sends output data to the presenter.
 */
public class SubmitEmailInteractor implements SubmitEmailInputBoundary {

    private static final int RISK_HIGH_SCORE = 100;
    private static final int RISK_MEDIUM_SCORE = 50;
    private static final int RISK_LOW_SCORE = 20;

    private final SubmitEmailOutputBoundary presenter;
    private final ExplainPhishingInputBoundary explainPhishingInteractor;

    /**
     * Create a new SubmitEmailInteractor.
     *
     * @param presenter                 the output boundary to present results
     * @param explainPhishingInteractor the explanation use case
     */
    public SubmitEmailInteractor(SubmitEmailOutputBoundary presenter,
                                 ExplainPhishingInputBoundary explainPhishingInteractor) {
        this.presenter = presenter;
        this.explainPhishingInteractor = explainPhishingInteractor;
    }

    /**
     * Map a {@link RiskLevel} to a numeric score.
     *
     * @param level the risk level
     * @return numeric risk score
     */
    private int mapRisk(RiskLevel level) {
        return switch (level) {
            case HIGH -> RISK_HIGH_SCORE;
            case MEDIUM -> RISK_MEDIUM_SCORE;
            case LOW -> RISK_LOW_SCORE;
            default -> 0;
        };
    }

    /**
     * Execute the submit email use case.
     *
     * @param inputData the raw email input data
     */
    @Override
    public void execute(SubmitEmailInputData inputData) {
        final String raw = inputData.getRawEmail();

        if (raw == null || raw.trim().isEmpty()) {
            final SubmitEmailOutputData out = new SubmitEmailOutputData(
                    "",
                    "",
                    0,
                    "",
                    "Please paste an email before analyzing."
            );
            presenter.present(out);
            return;
        }

        try {
            // Call the ExplainPhishing use case through its input boundary
            final ExplainPhishingInputData explainInput = new ExplainPhishingInputData(raw);
            final ExplainPhishingOutputData explainOutput =
                    explainPhishingInteractor.execute(explainInput);

            // Check if explanation was successful
            if (!explainOutput.isSuccess()) {
                presenter.present(new SubmitEmailOutputData(
                        "",
                        "",
                        0,
                        "",
                        explainOutput.getErrorMessage()
                ));
                return;
            }

            final PhishingExplanation explanation = explainOutput.getExplanation();

            final Email email = new EmailBuilder()
                    .body(raw)
                    .suspicionScore(mapRisk(explanation.getRiskLevel()))
                    .explanation(String.join("\n", explanation.getReasons()))
                    .links(explanation.getIndicators().getUrls())
                    .build();

            final SubmitEmailOutputData out = new SubmitEmailOutputData(
                    email.getTitle(),
                    email.getSender(),
                    (int) email.getSuspicionScore(),
                    email.getExplanation(),
                    null
            );
            presenter.present(out);

        }
        catch (IllegalStateException ex) {
            presenter.present(new SubmitEmailOutputData(
                    "",
                    "",
                    0,
                    "",
                    "OPENAI_API_KEY is not set on this machine."
            ));
        }
        catch (Exception ex) {
            presenter.present(new SubmitEmailOutputData(
                    "",
                    "",
                    0,
                    "",
                    "Unable to analyse this email. Please try again later."
            ));
        }
    }
}
