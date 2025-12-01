package infrastructure.services;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import use_case.interfaces.ExplanationException;

public class DeepSeekExplanationService extends LLMExplanationService {
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";
    private static final double DEFAULT_TEMPERATURE = 0.3;
    private static final int HTTP_OK = 200;

    private final String model;

    public DeepSeekExplanationService(String apiKey, String model) {
        super(apiKey);
        this.model = model;
    }

    @Override
    protected String callAPI(String prompt) throws Exception {
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        final JsonArray messages = new JsonArray();
        final JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", DEFAULT_TEMPERATURE);

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != HTTP_OK) {
            throw new ExplanationException("DeepSeek API error: " + response.statusCode());
        }

        final JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
        return responseJson.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}
