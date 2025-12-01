package use_case.submit_email;

import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;

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
    private final ExplainPhishingEmailUseCase explainPhishingEmailUseCase;

    /**
     * Create a new SubmitEmailInteractor.
     *
     * @param presenter the output boundary to present results
     * @param explainPhishingEmailUseCase the explanation use case
     */
    public SubmitEmailInteractor(SubmitEmailOutputBoundary presenter,
                                 ExplainPhishingEmailUseCase explainPhishingEmailUseCase) {
        this.presenter = presenter;
        this.explainPhishingEmailUseCase = explainPhishingEmailUseCase;
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
        }
        else {
            try {
                final PhishingExplanation explanation =
                        explainPhishingEmailUseCase.execute(raw);

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
            catch (ExplanationException ex) {
                presenter.present(new SubmitEmailOutputData(
                        "",
                        "",
                        0,
                        "",
                        "Failed to analyze email: " + ex.getMessage()
                ));
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
        }
    }
}
