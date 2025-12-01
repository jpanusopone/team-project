package config;

import java.util.Arrays;
import java.util.List;

import data_access.FirebaseEmailDataAccessObject;
import data_access.GoogleSafeBrowsingLinkRiskDataAccessObject;
import infrastructure.services.OpenRouterExplanationService;
import interface_adapter.link_risk.LinkRiskController;
import interface_adapter.link_risk.LinkRiskPresenter;
import interface_adapter.link_risk.LinkRiskViewModel;
import interface_adapter.save_email.SaveEmailController;
import interface_adapter.save_email.SaveEmailPresenter;
import interface_adapter.save_email.SaveEmailViewModel;
import presentation.ExplanationController;
import use_case.explain_phishing.ExplainPhishingInputBoundary;
import use_case.explain_phishing.ExplainPhishingInteractor;
import use_case.interfaces.ExplanationService;
import use_case.link_risk.LinkRiskInteractor;
import use_case.save_email.SaveEmailInteractor;
import view.SubmitEmailView;

public class ApplicationConfig {

    /**
     * Creates an ExplanationController wired to multiple LLM services via OpenRouter.
     *
     * @return the configured ExplanationController
     */
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

        final List<ExplanationService> services = Arrays.asList(
                new OpenRouterExplanationService(apiKey, "openai/gpt-4o-mini"),
                new OpenRouterExplanationService(apiKey, "deepseek/deepseek-chat"),
                new OpenRouterExplanationService(apiKey, "deepseek/deepseek-r1")
        );

        // Create ExplainPhishing use case with clean architecture
        final ExplainPhishingInputBoundary explainPhishingInteractor =
                new ExplainPhishingInteractor(services);
        return new ExplanationController(explainPhishingInteractor);
    }

    /**
     * Creates a fully wired SubmitEmailView with save-email and link-risk use cases.
     *
     * @return the configured SubmitEmailView
     */
    public static SubmitEmailView createSubmitEmailView() {
        // Create SaveEmail use case
        final SaveEmailViewModel saveEmailViewModel = new SaveEmailViewModel();
        final SaveEmailPresenter saveEmailPresenter = new SaveEmailPresenter(saveEmailViewModel);

        final FirebaseEmailDataAccessObject emailDao =
                new FirebaseEmailDataAccessObject();
        final SaveEmailInteractor saveEmailInteractor =
                new SaveEmailInteractor(emailDao, saveEmailPresenter);
        final SaveEmailController saveEmailController =
                new SaveEmailController(saveEmailInteractor);

        // Create LinkRisk use case with Google Safe Browsing API
        final LinkRiskViewModel linkRiskViewModel = new LinkRiskViewModel();
        final LinkRiskPresenter linkRiskPresenter = new LinkRiskPresenter(linkRiskViewModel);

        // Move Google Safe Browsing API key to environment variable
        final String googleApiKey = "AIzaSyB6wXISuyZxd6Q_LyqlWR10iOoMNXipSac";

        final GoogleSafeBrowsingLinkRiskDataAccessObject linkRiskDao =
                new GoogleSafeBrowsingLinkRiskDataAccessObject(googleApiKey);
        final LinkRiskInteractor linkRiskInteractor =
                new LinkRiskInteractor(linkRiskDao, linkRiskPresenter);
        final LinkRiskController linkRiskController =
                new LinkRiskController(linkRiskInteractor);

        // Create explanation controller
        final ExplanationController explanationController = createExplanationController();

        return new SubmitEmailView(
                explanationController,
                saveEmailController,
                saveEmailViewModel,
                linkRiskController,
                linkRiskViewModel
        );
    }
}
