package use_case.send_discord_update;

/**
 * Input Boundary for sending updates in Discord server.
 */
public interface SendDiscordUpdateInputBoundary {
    /**
     * Executes the use case.
     * @param sendDiscordUpdateInputData the input data
     */
    void excute(SendDiscordUpdateInputData sendDiscordUpdateInputData);
}
