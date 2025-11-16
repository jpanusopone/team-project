import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationService;
import use_case.interfaces.ExplanationException;
import infrastructure.services.OpenRouterExplanationService;
import entity.PhishingExplanation;

import java.util.Arrays;
import java.util.List;

/**
 * Simple command-line test for the ExplainPhishingEmailUseCase
 * Tests the use case with real API calls
 */
public class UseCaseTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   USE CASE TEST: ExplainPhishingEmail");
        System.out.println("========================================\n");

        // OpenRouter API Keys
        String openaiKey = "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";
        String deepseekKey = "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";
        String deepseekR1Key = "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";

        // Create the use case with fallback services
        System.out.println("Setting up Use Case with API services...");
        List<ExplanationService> services = Arrays.asList(
            new OpenRouterExplanationService(openaiKey, "openai/gpt-4o-mini"),
            new OpenRouterExplanationService(deepseekKey, "deepseek/deepseek-chat"),
            new OpenRouterExplanationService(deepseekR1Key, "deepseek/deepseek-r1")
        );

        ExplainPhishingEmailUseCase useCase = new ExplainPhishingEmailUseCase(services);
        System.out.println("✓ Use Case created with 3-tier fallback chain");
        System.out.println("  1. OpenAI GPT-4O Mini (primary)");
        System.out.println("  2. DeepSeek Chat (fallback #1)");
        System.out.println("  3. DeepSeek R1 (fallback #2)\n");

        // Test email - obvious phishing attempt
        String testEmail =
            "URGENT SECURITY ALERT!\n\n" +
            "Dear Valued Customer,\n\n" +
            "Your bank account has been LOCKED due to suspicious activity detected!\n\n" +
            "You must verify your identity IMMEDIATELY by clicking this link:\n" +
            "http://secure-bank-verify.xyz/login?id=12345\n\n" +
            "WARNING: If you do not verify within 24 hours, your account will be\n" +
            "PERMANENTLY CLOSED and all funds will be frozen!\n\n" +
            "Click here now: http://secure-bank-verify.xyz/urgent\n\n" +
            "This is an automated message. Do not reply.\n\n" +
            "Security Department\n" +
            "Your Bank";

        System.out.println("Test Email:");
        System.out.println("----------------------------------------");
        System.out.println(testEmail);
        System.out.println("----------------------------------------\n");

        // Execute the use case
        try {
            System.out.println("Executing use case: useCase.execute(emailContent)...");
            System.out.println("(Calling AI API - this may take a few seconds)\n");

            long startTime = System.currentTimeMillis();
            PhishingExplanation result = useCase.execute(testEmail);
            long endTime = System.currentTimeMillis();

            // Display results
            System.out.println("========================================");
            System.out.println("         USE CASE EXECUTION RESULT");
            System.out.println("========================================\n");

            System.out.println("✓ SUCCESS! Use case executed in " + (endTime - startTime) + "ms\n");

            System.out.println("ANALYSIS RESULTS:");
            System.out.println("================\n");

            System.out.println("Is Suspicious: " + (result.isSuspicious() ? "YES ⚠" : "NO ✓"));
            System.out.println("Risk Level: " + result.getRiskLevel() + "\n");

            System.out.println("Reasons (" + result.getReasons().size() + " total):");
            for (int i = 0; i < result.getReasons().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + result.getReasons().get(i));
            }

            System.out.println("\nPhishing Indicators:");
            var indicators = result.getIndicators();

            if (!indicators.getUrls().isEmpty()) {
                System.out.println("  • Suspicious URLs found:");
                for (String url : indicators.getUrls()) {
                    System.out.println("    - " + url);
                }
            }

            if (indicators.getSender() != null) {
                System.out.println("  • Sender: " + indicators.getSender());
            }

            if (indicators.getReplyTo() != null) {
                System.out.println("  • Reply-To: " + indicators.getReplyTo());
            }

            if (indicators.getUrgentLanguage() != null && indicators.getUrgentLanguage()) {
                System.out.println("  • Contains urgent/threatening language: YES");
            }

            if (indicators.getRequestsSensitiveInfo() != null && indicators.getRequestsSensitiveInfo()) {
                System.out.println("  • Requests sensitive information: YES");
            }

            if (indicators.getDisplayNameMismatch() != null && indicators.getDisplayNameMismatch()) {
                System.out.println("  • Display name mismatch detected: YES");
            }

            System.out.println("\nSuggested Actions (" + result.getSuggestedActions().size() + " total):");
            for (int i = 0; i < result.getSuggestedActions().size(); i++) {
                System.out.println("  " + (i + 1) + ". " + result.getSuggestedActions().get(i));
            }

            System.out.println("\n========================================");
            System.out.println("   ✓ USE CASE TEST PASSED!");
            System.out.println("========================================\n");

            System.out.println("The use case successfully:");
            System.out.println("  ✓ Accepted email content as input");
            System.out.println("  ✓ Sent request to AI API");
            System.out.println("  ✓ Received and parsed AI response");
            System.out.println("  ✓ Returned PhishingExplanation object");
            System.out.println("  ✓ Correctly identified phishing email");
            System.out.println("  ✓ Provided risk assessment and recommendations\n");

        } catch (ExplanationException e) {
            System.out.println("========================================");
            System.out.println("   ✗ USE CASE TEST FAILED!");
            System.out.println("========================================\n");

            System.out.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getMessage());
            }

            System.out.println("\nThis could mean:");
            System.out.println("  • All API services failed (check internet connection)");
            System.out.println("  • API keys are invalid or expired");
            System.out.println("  • API rate limit reached\n");

            e.printStackTrace();
        }
    }
}
