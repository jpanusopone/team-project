package use_case.link_risk;

public class LinkRiskApiException extends Exception {

    public LinkRiskApiException(String message) {
        super(message);
    }

    public LinkRiskApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
