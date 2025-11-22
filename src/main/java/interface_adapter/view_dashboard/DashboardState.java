package interface_adapter.view_dashboard;

import entity.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the current dashboard.
 */
public class DashboardState {

    private List<Email> emails = new ArrayList<>();

    public List<Email> getEmails() {
        return emails;
    }
}
