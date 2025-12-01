package data_access;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import config.FirebaseConfig;

/**
 * Firebase implementation for IT verification data access.
 * Handles IT staff authentication and email verification status updates.
 */
public class FirebaseITVerificationDataAccessObject implements ItVerificationGateway {
    private static final String COLLECTION_IT_ACCOUNTS = "it_accounts";
    private static final String COLLECTION_EMAILS = "emails";
    private static final String FIELD_USERNAME = "username";

    private final Firestore db;

    public FirebaseITVerificationDataAccessObject() {
        this.db = FirebaseConfig.getFirestore();
    }

    /**
     * Verify IT staff login credentials.
     *
     * @param username the IT staff username
     * @param password the IT staff password
     * @return true if credentials are valid, false otherwise
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public boolean verifyITLogin(String username, String password)
            throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_IT_ACCOUNTS)
                .whereEqualTo(FIELD_USERNAME, username)
                .whereEqualTo("password", password)
                .get();

        return !future.get().getDocuments().isEmpty();
    }

    /**
     * Create a new IT account (for initial setup).
     *
     * @param username the IT staff username
     * @param password the IT staff password
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public void createITAccount(String username, String password)
            throws ExecutionException, InterruptedException {
        final Map<String, Object> accountData = new HashMap<>();
        accountData.put(FIELD_USERNAME, username);
        accountData.put("password", password);
        accountData.put("createdAt", com.google.cloud.Timestamp.now());

        db.collection(COLLECTION_IT_ACCOUNTS).add(accountData).get();
    }

    /**
     * Update email verification status.
     *
     * @param emailId the email ID
     * @param status the verification status ("Pending", "Confirmed", "Safe")
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    @Override
    public void updateEmailVerificationStatus(String emailId, String status)
            throws ExecutionException, InterruptedException {
        final Map<String, Object> updates = new HashMap<>();
        updates.put("verifiedStatus", status);
        updates.put("verifiedAt", com.google.cloud.Timestamp.now());

        db.collection(COLLECTION_EMAILS).document(emailId).update(updates).get();
    }

    /**
     * Check if IT account exists.
     *
     * @param username the username to check
     * @return true if account exists, false otherwise
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public boolean itAccountExists(String username)
            throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_IT_ACCOUNTS)
                .whereEqualTo(FIELD_USERNAME, username)
                .get();

        return !future.get().getDocuments().isEmpty();
    }
}
