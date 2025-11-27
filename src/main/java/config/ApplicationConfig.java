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
        List<ExplanationService> services = Arrays.asList(
                new OpenRouterExplanationService(System.getenv("OPENAI_API_KEY"), "openai/gpt-4o-mini"),
                new OpenRouterExplanationService(System.getenv("DEEPSEEK_API_KEY"), "deepseek/deepseek-chat"),
                new OpenRouterExplanationService(System.getenv("DEEPSEEK_R1_API_KEY"), "deepseek/deepseek-r1")
        );

        ExplainPhishingEmailUseCase useCase = new ExplainPhishingEmailUseCase(services);
        return new ExplanationController(useCase);
    }
}