package data_access;

import java.util.concurrent.ExecutionException;

public interface ItVerificationGateway {
    void updateEmailVerificationStatus(String emailId, String status)
            throws ExecutionException, InterruptedException;
}
