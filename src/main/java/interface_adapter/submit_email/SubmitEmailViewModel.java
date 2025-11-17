package interface_adapter.submit_email;

public class SubmitEmailViewModel {

    private String title = "";
    private String sender = "";
    private String scoreText = "Score: —";
    private String explanation = "";
    private String errorMessage = null;

    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public String getScoreText() { return scoreText; }
    public String getExplanation() { return explanation; }
    public String getErrorMessage() { return errorMessage; }

    public void setTitle(String title) { this.title = title; }
    public void setSender(String sender) { this.sender = sender; }
    public void setScoreText(String scoreText) { this.scoreText = scoreText; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}