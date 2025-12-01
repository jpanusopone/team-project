package use_case.save_email;

/**
 * Output data for the Save Email use case.
 * Contains success status and a related message.
 */
public class SaveEmailOutputData {

    private final boolean success;
    private final String message;

    /**
     * Constructs a SaveEmailOutputData object.
     *
     * @param success whether the operation was successful
     * @param message message describing the outcome
     */
    public SaveEmailOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * Returns whether the save operation was successful.
     *
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message describing the result.
     *
     * @return the outcome message
     */
    public String getMessage() {
        return message;
    }
}
