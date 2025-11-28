package use_case.filter;

import java.util.List;

import entity.Email;

/**
 * Output Data for the Filter Use Case.
 */
public class FilterOutputData {
    private final List<Email> emails;

    public FilterOutputData(List<Email> emails) {
        this.emails = emails;
    }

    public List<Email> getEmails() {
        return emails;
    }

}
