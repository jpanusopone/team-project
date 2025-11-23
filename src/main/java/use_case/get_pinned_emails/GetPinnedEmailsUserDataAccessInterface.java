package use_case.get_pinned_emails;

import entity.Email;

import java.util.List;

/**
 * DAO interface for the ViewDashboard Use Case.
 */
public interface GetPinnedEmailsUserDataAccessInterface {

    /**
     * Updates the system to display the pinned emails.
     * @param inputData the emails to
     * @return
     */
    List<Email> getPinnedEmails(GetPinnedEmailsInputData inputData);
}
