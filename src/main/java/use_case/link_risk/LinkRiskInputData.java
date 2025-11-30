package use_case.link_risk;

public class LinkRiskInputData {
    private final String emailText;

    public LinkRiskInputData(String emailText) {
        this.emailText = emailText;
    }

    public String getEmailText() {
        return emailText;
    }
}
