import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationService;
import use_case.interfaces.ExplanationException;
import infrastructure.services.OpenRouterExplanationService;
import entity.PhishingExplanation;

import java.util.Arrays;
import java.util.List;

/**
 * Simple test to verify ExplainPhishingEmailUseCase works
 * Modify the emailContent variable to test your own emails
 */
public class TestMyEmail {

    public static void main(String[] args) {
        // ============================================
        // MODIFY THIS SECTION TO TEST YOUR OWN EMAIL
        // ============================================

        String emailContent =
            "Subject: YOUR ACCOUNT IS AT RISK!!\n\n" +
            "Dear Valued User,\n\n" +
            "We received a request from you to terminate your Office 365 email due to a dual college/universities account. " +
            "This process has begun by our administrator. If you did not authorize this action and you have no knowledge of it, " +
            "you are advised to re-verify your account. Please give us 24 hours to terminate your account if you initiated the request. " +
            "Failure to re-verify will result in the closure of your account and you will lose all of your files on these 365 accounts.\n\n" +
            "If this request was made accidentally and you have no knowledge of it, you are advised to copy and paste the URL below " +
            "into the address bar of your web browser to fill in the form.\n\n" +
            "Verify Now: cutt.ly/0wtNi6KO\n\n" +
            "Failure to verify will result in the closure of your account.\n\n" +
            "Iowa State University\n" +
            "IT Helpdesk All Rights Reserved.";

        // ============================================
        // END OF MODIFIABLE SECTION
        // ============================================

        System.out.println("Testing ExplainPhishingEmailUseCase...\n");
        System.out.println("Email Content:");
        System.out.println("----------------------------------------");
        System.out.println(emailContent);
        System.out.println("----------------------------------------\n");

        try {
            // Setup the use case
            List<ExplanationService> services = Arrays.asList(
                new OpenRouterExplanationService(
                    "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",
                    "openai/gpt-4o-mini"
                )
            );
            ExplainPhishingEmailUseCase useCase = new ExplainPhishingEmailUseCase(services);

            // Execute the use case
            System.out.println("Calling useCase.execute()...\n");
            PhishingExplanation result = useCase.execute(emailContent);

            // Display results
            System.out.println("✓ USE CASE EXECUTED SUCCESSFULLY\n");
            System.out.println("Results:");
            System.out.println("--------");
            System.out.println("Suspicious: " + result.isSuspicious());
            System.out.println("Risk Level: " + result.getRiskLevel());
            System.out.println("\nReasons:");
            for (int i = 0; i < result.getReasons().size(); i++) {
                System.out.println("  " + (i+1) + ". " + result.getReasons().get(i));
            }
            System.out.println("\nSuggested Actions:");
            for (int i = 0; i < result.getSuggestedActions().size(); i++) {
                System.out.println("  " + (i+1) + ". " + result.getSuggestedActions().get(i));
            }


        } catch (ExplanationException e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
