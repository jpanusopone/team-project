package use_case.filter;

import entity.Email;
import java.util.List;

/**
 * Output Data for the Filter Use Case.
 */
public class FilterOutputData {
    List<Email> emails;

    public FilterOutputData(List<Email> emails) {
        this.emails = emails;
    }

    public List<Email> getEmails() {
        return emails;
    }

}
