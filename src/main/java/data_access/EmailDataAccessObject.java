package data_access;

import entity.Email;
import use_case.get_pinned_emails.GetPinnedEmailsInputData;
import use_case.get_pinned_emails.GetPinnedEmailsUserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class EmailDataAccessObject implements GetPinnedEmailsUserDataAccessInterface {

    private final List<Email> database = new ArrayList<>();

    @Override
    public List<Email> getPinnedEmails() {
        return null;
    }
}
