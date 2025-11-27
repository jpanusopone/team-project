package use_case.submit_email;

public class SubmitEmailInputData {
    private final String rawEmail;

    public SubmitEmailInputData(String rawEmail) {
        this.rawEmail = rawEmail;
    }

    public String getRawEmail() {
        return rawEmail;
    }
}