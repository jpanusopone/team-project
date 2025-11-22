package use_case.submit_email;

import entity.Email;
import entity.EmailBuilder;

public class SubmitEmailInteractor implements SubmitEmailInputBoundary {

    private final SubmitEmailOutputBoundary presenter;
    private final ChatGPTAnalyzer analyzer;

    public SubmitEmailInteractor(SubmitEmailOutputBoundary presenter) {
        this.presenter = presenter;
        this.analyzer = new ChatGPTAnalyzer();
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
            ChatGPTAnalyzer.Result r = analyzer.analyze(raw);

            Email email = new EmailBuilder()
                    .setBody(raw)
                    .setSuspicionScore(r.score)
                    .setExplanation(r.explanation)
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