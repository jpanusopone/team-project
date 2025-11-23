package interface_adapter.view_dashboard;

import entity.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the current dashboard.
 */
public class DashboardState {
    private List<Email> pinnedEmails = new ArrayList<>();
    private String error = null;

    public List<Email> getPinnedEmails() {
        return pinnedEmails;
    }

    public void setPinnedEmails(List<Email> pinnedEmails) {
        this.pinnedEmails = pinnedEmails;
    }

    private List<Email> emails = new ArrayList<>();

    public List<Email> getEmails() {
        return emails;
    }

    public String getError() {
        return error;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public void setError(String error) {
        this.error = error;
    }
}
