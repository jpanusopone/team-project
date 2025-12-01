package infrastructure.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import use_case.interfaces.ExplanationException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

/**
 * OpenRouter API service - unified gateway for multiple LLM providers
 * Supports: OpenAI models, DeepSeek models, and many others
 */
public class OpenRouterExplanationService extends LLMExplanationService {
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private final String model;

    public OpenRouterExplanationService(String apiKey, String model) {
        super(apiKey);
        this.model = model;
    }

    @Override
    protected String callApi(String prompt) throws Exception {
        final JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        final JsonArray messages = new JsonArray();
        final JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.3);

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                // Required by OpenRouter
                .header("HTTP-Referer", "https://github.com/team-project")
                // Optional but recommended
                .header("X-Title", "Phishing Email Analyzer")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        final int exceptionStatusCode = 200;
        if (response.statusCode() != exceptionStatusCode) {
            throw new ExplanationException("OpenRouter API error: " + response.statusCode() + " - " + response.body());
        }

        final JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
        return responseJson.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }
}
