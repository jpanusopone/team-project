package use_case.explain_phishing;

public class ExplainPhishingInputData {
    private final String emailContent;

    public ExplainPhishingInputData(String emailContent) {
        this.emailContent = emailContent;
    }

    public String getEmailContent() {
        return emailContent;
    }
}
