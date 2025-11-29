import config.FirebaseConfig;
import data_access.FirebaseITVerificationDataAccessObject;

/**
 * Utility to create an IT account in Firebase.
 * Run this once to set up a test IT account.
 */
public class CreateITAccount {
    public static void main(String[] args) {
        try {
            // Initialize Firebase
            System.out.println("Initializing Firebase...");
            FirebaseConfig.initialize();
            System.out.println("✓ Firebase initialized successfully!\n");

            // Create IT account
            FirebaseITVerificationDataAccessObject itDAO = new FirebaseITVerificationDataAccessObject();

            String username = "admin";
            String password = "admin123";

            // Check if account already exists
            if (itDAO.itAccountExists(username)) {
                System.out.println("⚠ IT account '" + username + "' already exists!");
                System.out.println("You can login with the existing credentials.");
            } else {
                System.out.println("Creating IT account...");
                itDAO.createITAccount(username, password);
                System.out.println("✓ IT account created successfully!\n");
                System.out.println("Login credentials:");
                System.out.println("  Username: " + username);
                System.out.println("  Password: " + password);
            }

        } catch (Exception e) {
            System.err.println("\n✗ FAILED to create IT account!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
