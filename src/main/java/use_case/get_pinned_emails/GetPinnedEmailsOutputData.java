package use_case.get_pinned_emails;

import entity.Email;

import java.util.List;

public class GetPinnedEmailsOutputData {
    private final List<Email> pinnedEmails;

    public GetPinnedEmailsOutputData(List<Email> pinnedEmails) {
        this.pinnedEmails = pinnedEmails;
    }

    public List<Email> getPinnedEmails() {
        return pinnedEmails;
    }
}
