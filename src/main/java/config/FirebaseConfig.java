package config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase configuration and initialization.
 * Provides singleton access to Firestore database.
 */
public class FirebaseConfig {
    private static Firestore firestore;
    private static boolean initialized = false;

    /**
     * Initialize Firebase with service account credentials.
     * The credentials file should be placed in the project root or specified via environment variable.
     *
     * @throws IOException if credentials file cannot be read
     */
    public static void initialize() throws IOException {
        if (initialized) {
            return;
        }

        try {
            // Try to get credentials from environment variable first
            String credentialsPath = System.getenv("FIREBASE_CREDENTIALS");
            InputStream serviceAccount;

            if (credentialsPath != null && !credentialsPath.isEmpty()) {
                serviceAccount = new FileInputStream(credentialsPath);
            } else {
                // Fallback to default location
                serviceAccount = new FileInputStream("firebase-credentials.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            firestore = FirestoreClient.getFirestore();
            initialized = true;

        } catch (IOException e) {
            throw new IOException("Failed to initialize Firebase. Please ensure firebase-credentials.json exists " +
                    "or set FIREBASE_CREDENTIALS environment variable.", e);
        }
    }

    /**
     * Get Firestore database instance.
     * Automatically initializes Firebase if not already initialized.
     *
     * @return Firestore database instance
     */
    public static Firestore getFirestore() {
        if (!initialized) {
            try {
                initialize();
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize Firebase", e);
            }
        }
        return firestore;
    }

    /**
     * Check if Firebase is initialized.
     *
     * @return true if initialized, false otherwise
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
