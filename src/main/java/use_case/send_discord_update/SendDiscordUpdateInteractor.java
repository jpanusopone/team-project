package use_case.send_discord_update;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Tbe Send Discord Update Interactor.
 */
public class SendDiscordUpdateInteractor implements SendDiscordUpdateInputBoundary{
    private final String webhookUrl;
    private final OkHttpClient client;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
//    private final SendDiscordUpdateOutputData sendDiscordUpdateOutputData;

    public SendDiscordUpdateInteractor() throws IOException {
        webhookUrl = Files.readString(Path.of("discord_webhook_url.txt")).trim();
        client = new OkHttpClient();
//        this.sendDiscordUpdateOutputData = sendDiscordUpdateOutputData;
    }

    @Override
    public void excute(SendDiscordUpdateInputData sendDiscordUpdateInputData) {
//        String jsonPayload = "{\"content\": \"test\"}";
        String jsonPayload = buildJsonPayload("test");

        RequestBody body = RequestBody.create(jsonPayload, JSON);

        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

//            if (!response.isSuccessful()) {
//                try {
//                    throw new IOException(
//                            "Failed to send Discord webhook: " +
//                                    response.code() + " " + response.message()
//                    );
//                } catch (IOException ex) {
//                    ex.printStackTrace();   // Replace with logging if needed
//                }
//                return;  // Optional: stop here
//            }

            // Optionally handle successful case
            System.out.println("Message sent successfully.");

        } catch (IOException e) {
            // This catches connection errors, timeouts, and internal OkHttp errors
            e.printStackTrace();
        }

//        try {
//            URL url = new URL(webhookUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setDoOutput(true);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            int responseCode = conn.getResponseCode();
//            System.out.println("Discord response code: " + responseCode);
//            // Optionally read response using conn.getInputStream() if needed
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Builds the minimal JSON Discord expects: {"content": "..."}
     * Escapes backslashes, quotes and newlines so it's valid JSON.
     */
    private String buildJsonPayload(String message) {
        String escaped = message
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");

        return "{\"content\":\"" + escaped + "\"}";
    }
}
