package use_case.send_discord_update;

/**
 * Output Data for sending updates in Discord server.
 */
public class SendDiscordUpdateOutputData {
    private final boolean success;
    private final String errorMessage;  // null if success

    public SendDiscordUpdateOutputData(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
