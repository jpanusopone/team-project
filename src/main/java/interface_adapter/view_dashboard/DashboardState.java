package interface_adapter.view_dashboard;

import entity.Email;
<<<<<<< HEAD
=======

>>>>>>> d97ad4a (Testing connection between MVC components for filter use case)
import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the current dashboard.
 */
public class DashboardState {
<<<<<<< HEAD
    private List<Email> pinnedEmails = new ArrayList<>();
    private String error = null;

    public List<Email> getPinnedEmails() {
        return pinnedEmails;
    }

    public void setPinnedEmails(List<Email> pinnedEmails) {
        this.pinnedEmails = pinnedEmails;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
=======

    private List<Email> emails = new ArrayList<>();

    public List<Email> getEmails() {
        return emails;
>>>>>>> d97ad4a (Testing connection between MVC components for filter use case)
    }
}
