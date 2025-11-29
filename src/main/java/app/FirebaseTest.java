package app;

import config.FirebaseConfig;
import data_access.FirebaseEmailDataAccessObject;
import entity.Email;
import entity.EmailBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Simple test class to verify Firebase connection.
 * Run this to test if Firebase is working correctly.
 */
public class FirebaseTest {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            System.out.println("Initializing Firebase...");
            FirebaseConfig.initialize();
            System.out.println("✓ Firebase initialized successfully!");

            // Create a test email
            FirebaseEmailDataAccessObject emailDAO = new FirebaseEmailDataAccessObject();

            EmailBuilder builder = new EmailBuilder();
            Email testEmail = builder
                    .title("Test Phishing Email")
                    .sender("suspicious@phishing-test.com")
                    .body("This is a test email to verify Firebase connection")
                    .dateReceived(LocalDateTime.now())
                    .suspicionScore(90.5)
                    .pinned(false)
                    .pinnedDate(LocalDateTime.now())
                    .explanation("Test explanation")
                    .links(Arrays.asList("http://suspicious-link.com"))
                    .verifiedStatus("Pending")
                    .build();

            // Save to Firebase
            System.out.println("\nSaving test email to Firebase...");
            Email savedEmail = emailDAO.saveEmail(testEmail);
            System.out.println("✓ Email saved successfully!");

            // Retrieve all emails
            System.out.println("\nRetrieving all emails from Firebase...");
            var allEmails = emailDAO.getAllEmails();
            System.out.println("✓ Found " + allEmails.size() + " email(s) in database");

            // Retrieve pinned emails
            System.out.println("\nRetrieving pinned emails...");
            var pinnedEmails = emailDAO.getPinnedEmails();
            System.out.println("✓ Found " + pinnedEmails.size() + " pinned email(s)");

            System.out.println("\n========================================");
            System.out.println("✓ ALL TESTS PASSED!");
            System.out.println("Firebase is working correctly!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("\n✗ TEST FAILED!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nTroubleshooting:");
            System.err.println("1. Make sure firebase-credentials.json exists in project root");
            System.err.println("2. Check that you created a Firestore database in Firebase Console");
            System.err.println("3. Verify your internet connection");
        }
    }
}
