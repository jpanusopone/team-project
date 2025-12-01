package use_case.submit_email;

/**
 * Data transfer object for the output of the Submit Email use case.
 * Holds analysis results and any error message produced during processing.
 */
public class SubmitEmailOutputData {

    private final String title;
    private final String sender;
    private final int score;
    private final String explanation;
    private final String errorMessage;

    /**
     * Constructs output data for the email analysis result.
     *
     * @param title        the extracted email subject
     * @param sender       the extracted sender address
     * @param score        the calculated suspicion score
     * @param explanation  the explanation text
     * @param errorMessage the error message, or null if no error
     */
    public SubmitEmailOutputData(
            String title,
            String sender,
            int score,
            String explanation,
            String errorMessage
    ) {
        this.title = title;
        this.sender = sender;
        this.score = score;
        this.explanation = explanation;
        this.errorMessage = errorMessage;
    }

    /**
     * Get the email title extracted from the message.
     *
     * @return the email title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the sender address extracted from the message.
     *
     * @return the email sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get the numeric suspicion score for this email.
     *
     * @return the suspicion score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get the explanation for the assigned suspicion score.
     *
     * @return the explanation text
     */
    public String getExplanation() {
        return explanation;
    }

    /**
     * Get the error message produced during analysis, if any.
     *
     * @return the error message, or {@code null} if no error occurred
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Determine whether this output represents an error state.
     *
     * @return {@code true} if an error message is present; {@code false} otherwise
     */
    public boolean hasError() {
        return errorMessage != null;
    }
}
