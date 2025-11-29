package data_access;

import entity.Email;
import use_case.get_pinned_emails.GetPinnedEmailsInputData;
import use_case.get_pinned_emails.GetPinnedEmailsUserDataAccessInterface;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Data access object for getting pinned emails using Firebase.
 */
public class GetPinnedEmailsDataAccessObject implements GetPinnedEmailsUserDataAccessInterface {
    private final FirebaseEmailDataAccessObject emailDAO;

    public GetPinnedEmailsDataAccessObject() {
        this.emailDAO = new FirebaseEmailDataAccessObject();
    }

    @Override
    public List<Email> getPinnedEmails(GetPinnedEmailsInputData inputData) {
        try {
            // Get all pinned emails from Firebase
            return emailDAO.getPinnedEmails();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to retrieve pinned emails from Firebase", e);
        }
    }
}
