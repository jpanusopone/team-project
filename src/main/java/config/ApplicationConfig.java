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

    public static view.SubmitEmailView createSubmitEmailView() {
        // Create SaveEmail use case
        interface_adapter.save_email.SaveEmailViewModel saveEmailViewModel =
            new interface_adapter.save_email.SaveEmailViewModel();
        interface_adapter.save_email.SaveEmailPresenter saveEmailPresenter =
            new interface_adapter.save_email.SaveEmailPresenter(saveEmailViewModel);

        data_access.FirebaseEmailDataAccessObject emailDAO =
            new data_access.FirebaseEmailDataAccessObject();
        use_case.save_email.SaveEmailInteractor saveEmailInteractor =
            new use_case.save_email.SaveEmailInteractor(emailDAO, saveEmailPresenter);
        interface_adapter.save_email.SaveEmailController saveEmailController =
            new interface_adapter.save_email.SaveEmailController(saveEmailInteractor);

        // Create LinkRisk use case with Google Safe Browsing API
        interface_adapter.link_risk.LinkRiskViewModel linkRiskViewModel =
            new interface_adapter.link_risk.LinkRiskViewModel();
        interface_adapter.link_risk.LinkRiskPresenter linkRiskPresenter =
            new interface_adapter.link_risk.LinkRiskPresenter(linkRiskViewModel);

        // Google Safe Browsing API key (hardcoded for now)
        String googleApiKey = "AIzaSyB6wXISuyZxd6Q_LyqlWR10iOoMNXipSac"; // TODO: Move to environment variable

        data_access.GoogleSafeBrowsingLinkRiskDataAccessObject linkRiskDAO =
            new data_access.GoogleSafeBrowsingLinkRiskDataAccessObject(googleApiKey);
        use_case.link_risk.LinkRiskInteractor linkRiskInteractor =
            new use_case.link_risk.LinkRiskInteractor(linkRiskDAO, linkRiskPresenter);
        interface_adapter.link_risk.LinkRiskController linkRiskController =
            new interface_adapter.link_risk.LinkRiskController(linkRiskInteractor);

        // Create explanation controller
        presentation.ExplanationController explanationController = createExplanationController();

        return new view.SubmitEmailView(explanationController, saveEmailController,
                                       saveEmailViewModel, linkRiskController, linkRiskViewModel);
    }
}
