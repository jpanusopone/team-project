package infrastructure.services;

import use_case.interfaces.ExplanationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class OpenAIExplanationService extends LLMExplanationService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String model;

    public OpenAIExplanationService(String apiKey, String model) {
        super(apiKey);
        this.model = model; // "gpt-4o-mini-2024-07-18" or your model
    }

    @Override
    protected String callAPI(String prompt) throws Exception {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", model);

        JsonArray messages = new JsonArray();
        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", prompt);
        messages.add(message);

        requestBody.add("messages", messages);
        requestBody.addProperty("temperature", 0.3);
        requestBody.add("response_format", gson.toJsonTree(new ResponseFormat("json_object")));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new ExplanationException("OpenAI API error: " + response.statusCode());
        }

        JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
        return responseJson.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();
    }

    private static class ResponseFormat {
        String type;
        ResponseFormat(String type) { this.type = type; }
    }
}
