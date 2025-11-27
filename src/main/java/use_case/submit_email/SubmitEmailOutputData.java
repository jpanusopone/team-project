package use_case.submit_email;

public class SubmitEmailOutputData {

    private final String title;
    private final String sender;
    private final int score;
    private final String explanation;
    private final String errorMessage; // null if OK

    public SubmitEmailOutputData(String title,
                                 String sender,
                                 int score,
                                 String explanation,
                                 String errorMessage) {
        this.title = title;
        this.sender = sender;
        this.score = score;
        this.explanation = explanation;
        this.errorMessage = errorMessage;
    }

    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public int getScore() { return score; }
    public String getExplanation() { return explanation; }
    public String getErrorMessage() { return errorMessage; }

    public boolean hasError() {
        return errorMessage != null;
    }
}