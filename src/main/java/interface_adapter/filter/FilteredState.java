package interface_adapter.filter;

import entity.Email;
import use_case.filter.SortBy;
import java.util.List;

/**
 * The State information representing the current filters.
 */
public class FilteredState {
    private List<Email> emails;
    private String error;

    public FilteredState(List<Email> emails) {
        this.emails = emails;
        this.error = null;
    }

    public FilteredState() {

    }

    // --- GETTERS ---

    public List<Email> getEmails() {
        return emails;
    }

    public String getError() {
        return error;
    }


    // --- SETTERS ---

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public void setError(String error) {
        this.error = error;
    }

}
