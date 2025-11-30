package use_case.submit_email;

import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;

public class SubmitEmailInteractor implements SubmitEmailInputBoundary {

    private final SubmitEmailOutputBoundary presenter;
    private final ExplainPhishingEmailUseCase explainPhishingEmailUseCase;

    public SubmitEmailInteractor(SubmitEmailOutputBoundary presenter,
                                 ExplainPhishingEmailUseCase explainPhishingEmailUseCase) {
        this.presenter = presenter;
        this.explainPhishingEmailUseCase = explainPhishingEmailUseCase;
    }

    private int mapRisk(RiskLevel level) {
        switch (level) {
            case HIGH:
                return 100;
            case MEDIUM:
                return 50;
            case LOW:
                return 20;
            default:
                return 0;
        }
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
            PhishingExplanation expl = explainPhishingEmailUseCase.execute(raw);

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

        } catch (ExplanationException e) {
            presenter.present(new SubmitEmailOutputData(
                    "", "", 0, "",
                    "Failed to analyze email: " + e.getMessage()
            ));
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
