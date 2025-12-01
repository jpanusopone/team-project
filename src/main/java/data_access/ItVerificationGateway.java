package data_access;

/**
 * Gateway interface for IT verification operations.
 * Provides a method for updating the verification status of an email.
 */
public interface ItVerificationGateway {

    /**
     * Update the verification status of an email in the database.
     *
     * @param emailId the ID of the email to update
     * @param status the new verification status (e.g., "Pending", "Confirmed", "Safe")
     * @throws java.util.concurrent.ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    void updateEmailVerificationStatus(String emailId, String status)
            throws java.util.concurrent.ExecutionException, InterruptedException;
}
