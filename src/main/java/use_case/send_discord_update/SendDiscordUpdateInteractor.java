package use_case.send_discord_update;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tbe Send Discord Update Interactor.
 */
public class SendDiscordUpdateInteractor implements SendDiscordUpdateInputBoundary{
    private final String webhookUrl;
    private final SendDiscordUpdateOutputData sendDiscordUpdateOutputData;

    public SendDiscordUpdateInteractor(SendDiscordUpdateOutputData sendDiscordUpdateOutputData) throws IOException {
        webhookUrl = Files.readString(Path.of("discord_webhook_url.txt")).trim();
        this.sendDiscordUpdateOutputData = sendDiscordUpdateOutputData;
    }

    @Override
    public void excute(SendDiscordUpdateInputData sendDiscordUpdateInputData) {
        String jsonPayload = "{\"content\": \"test\"}";

        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Discord response code: " + responseCode);
            // Optionally read response using conn.getInputStream() if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
