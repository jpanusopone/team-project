package interface_adapter.view_dashboard;

import entity.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the current dashboard.
 */
public class DashboardState {

    private List<Email> emails = new ArrayList<>();
    private String error;

    public DashboardState(DashboardState copy) {
        emails = copy.emails;
        error = copy.error;
    }

    public DashboardState() {

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
