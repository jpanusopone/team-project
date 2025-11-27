package infrastructure.services;

import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import use_case.interfaces.ExplanationService;
import use_case.interfaces.ExplanationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;

public abstract class LLMExplanationService implements ExplanationService {
    protected final HttpClient httpClient;
    protected final Gson gson;
    protected final String apiKey;

    protected LLMExplanationService(String apiKey) {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
        this.apiKey = apiKey;
    }

    @Override
    public PhishingExplanation explainEmail(String emailContent) throws ExplanationException {
        try {
            String prompt = buildPrompt(emailContent);
            String responseJson = callAPI(prompt);
            return parseResponse(responseJson);
        } catch (Exception e) {
            throw new ExplanationException("Failed to get explanation", e);
        }
    }

    protected String buildPrompt(String emailContent) {
        return "Analyze this email for phishing indicators and respond ONLY with valid JSON matching this exact structure:\n\n" +
                "{\n" +
                "  \"suspicious\": boolean,\n" +
                "  \"risk_level\": \"low\" | \"medium\" | \"high\",\n" +
                "  \"reasons\": [\"reason1\", \"reason2\"],\n" +
                "  \"indicators\": {\n" +
                "    \"urls\": [\"url1\"],\n" +
                "    \"sender\": \"email@domain.com or null\",\n" +
                "    \"reply_to\": \"email@domain.com or null\",\n" +
                "    \"display_name_mismatch\": true/false/null,\n" +
                "    \"urgent_language\": true/false/null,\n" +
                "    \"requests_sensitive_info\": true/false/null,\n" +
                "    \"attachments\": [\"file1.exe\"]\n" +
                "  },\n" +
                "  \"suggested_actions\": [\"action1\", \"action2\"]\n" +
                "}\n\n" +
                "Email to analyze:\n" + emailContent;
    }

    protected abstract String callAPI(String prompt) throws Exception;

    protected PhishingExplanation parseResponse(String responseJson) {
        // Handle case where response might be a JSON string (e.g., DeepSeek returns "{ ... }" as a string)
        JsonObject root;
        try {
            root = gson.fromJson(responseJson, JsonObject.class);
        } catch (Exception e) {
            // If direct parsing fails, try parsing as string first
            String jsonContent = gson.fromJson(responseJson, String.class);
            root = gson.fromJson(jsonContent, JsonObject.class);
        }

        boolean suspicious = root.get("suspicious").getAsBoolean();
        RiskLevel riskLevel = RiskLevel.valueOf(root.get("risk_level").getAsString().toUpperCase());

        List<String> reasons = gson.fromJson(root.get("reasons"), List.class);
        List<String> suggestedActions = gson.fromJson(root.get("suggested_actions"), List.class);

        JsonObject indicatorsJson = root.getAsJsonObject("indicators");
        PhishingIndicators indicators = new PhishingIndicators(
                gson.fromJson(indicatorsJson.get("urls"), List.class),
                getStringOrNull(indicatorsJson, "sender"),
                getStringOrNull(indicatorsJson, "reply_to"),
                getBooleanOrNull(indicatorsJson, "display_name_mismatch"),
                getBooleanOrNull(indicatorsJson, "urgent_language"),
                getBooleanOrNull(indicatorsJson, "requests_sensitive_info"),
                gson.fromJson(indicatorsJson.get("attachments"), List.class)
        );

        return new PhishingExplanation(suspicious, riskLevel, reasons, indicators, suggestedActions);
    }

    private String getStringOrNull(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : null;
    }

    private Boolean getBooleanOrNull(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsBoolean() : null;
    }
}
