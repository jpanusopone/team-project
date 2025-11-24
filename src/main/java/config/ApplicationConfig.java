package config;

import infrastructure.services.*;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationService;
import presentation.ExplanationController;

import java.util.Arrays;
import java.util.List;

public class ApplicationConfig {

    public static ExplanationController createExplanationController() {
        // Create services in priority order (fallback chain)
        // Using OpenRouter API gateway for unified access to multiple LLMs
        // Priority: OpenAI GPT-4O Mini -> DeepSeek Chat -> DeepSeek R1

        // Try to get API key from environment variable, fallback to hardcoded key if not set
        String apiKey = System.getenv("OPENROUTER_API_KEY");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            // Fallback to hardcoded API key (same as in TestMyEmail.java)
            apiKey = "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";
        }

        List<ExplanationService> services = Arrays.asList(
                new OpenRouterExplanationService(apiKey, "openai/gpt-4o-mini"),
                new OpenRouterExplanationService(apiKey, "deepseek/deepseek-chat"),
                new OpenRouterExplanationService(apiKey, "deepseek/deepseek-r1")
        );

        ExplainPhishingEmailUseCase useCase = new ExplainPhishingEmailUseCase(services);
        return new ExplanationController(useCase);
    }
}