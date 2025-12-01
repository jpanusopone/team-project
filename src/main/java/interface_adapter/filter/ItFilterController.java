package interface_adapter.filter;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import data_access.FirebaseEmailDataAccessObject;
import entity.Email;
import interface_adapter.it_dashboard.ItDashboardController;
import view.ItDashboardView;

/**
 * Controller responsible for filtering and displaying pinned emails
 * in the IT dashboard view.
 */
public class ItFilterController {

    private final ItDashboardView view;
    private final FirebaseEmailDataAccessObject emailDao;
    private final ItDashboardController dashboardController;

    /**
     * Construct an IT filter controller.
     *
     * @param view                the IT dashboard view
     * @param dashboardController the IT dashboard controller
     */
    public ItFilterController(ItDashboardView view,
                              ItDashboardController dashboardController) {
        this.view = view;
        this.emailDao = new FirebaseEmailDataAccessObject();
        this.dashboardController = dashboardController;

        // Filter button applies filter
        view.getItFilterButton().addActionListener(event -> applyFilter());

        // Discord button behavior is handled inside ItDashboardView

        loadAllPinnedEmails();
    }

    /**
     * Load all pinned emails and display them in the table.
     */
    private void loadAllPinnedEmails() {
        try {
            final List<Email> emails = emailDao.getPinnedEmails();
            displayEmails(emails);
        }
        catch (ExecutionException | InterruptedException ex) {
            JOptionPane.showMessageDialog(view,
                    "Failed to load emails: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Apply keyword, sender, and sorting filters, then update the table.
     */
    private void applyFilter() {
        try {
            final String keyword = view.getKeyword();
            final String sender = view.getSender();
            final String sortBy = view.getSort();

            final List<Email> emails = emailDao.getPinnedEmails();

            filterByKeyword(emails, keyword);
            filterBySender(emails, sender);
            sortEmails(emails, sortBy);

            displayEmails(emails);
        }
        catch (ExecutionException | InterruptedException ex) {
            JOptionPane.showMessageDialog(view,
                    "Failed to filter emails: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Filter emails list by keyword, if provided.
     *
     * @param emails  the list of emails to filter (modified in place)
     * @param keyword the keyword input
     */
    private void filterByKeyword(List<Email> emails, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            final String lowerKeyword = keyword.toLowerCase();
            emails.removeIf(email -> !matchesKeyword(email, lowerKeyword));
        }
    }

    /**
     * Filter emails list by sender, if provided.
     *
     * @param emails the list of emails to filter (modified in place)
     * @param sender the sender input
     */
    private void filterBySender(List<Email> emails, String sender) {
        if (sender != null && !sender.isEmpty()) {
            final String lowerSender = sender.toLowerCase();
            emails.removeIf(email -> !matchesSender(email, lowerSender));
        }
    }

    /**
     * Sort emails list based on the sort option.
     *
     * @param emails the list of emails to sort (modified in place)
     * @param sortBy the sort option string
     */
    private void sortEmails(List<Email> emails, String sortBy) {
        if (sortBy != null) {
            switch (sortBy) {
                case "Date":
                    emails.sort(Comparator.comparing(Email::getDateReceived).reversed());
                    break;
                case "Sender":
                    emails.sort(Comparator.comparing(Email::getSender));
                    break;
                case "Suspicion Score":
                    emails.sort(Comparator.comparing(Email::getSuspicionScore).reversed());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Check if an email matches the given keyword.
     *
     * @param email        the email to check
     * @param lowerKeyword the keyword in lowercase
     * @return true if any of title/sender/body contains the keyword
     */
    private boolean matchesKeyword(Email email, String lowerKeyword) {
        final String titleLower = toLowerOrEmpty(email.getTitle());
        final String senderLower = toLowerOrEmpty(email.getSender());
        final String bodyLower = toLowerOrEmpty(email.getBody());

        return titleLower.contains(lowerKeyword)
                || senderLower.contains(lowerKeyword)
                || bodyLower.contains(lowerKeyword);
    }

    /**
     * Check if an email matches the given sender filter.
     *
     * @param email       the email to check
     * @param lowerSender the sender substring in lowercase
     * @return true if sender contains the substring
     */
    private boolean matchesSender(Email email, String lowerSender) {
        final String senderLower = toLowerOrEmpty(email.getSender());
        return senderLower.contains(lowerSender);
    }

    /**
     * Convert a string to lowercase, or return an empty string if null.
     *
     * @param value the input string
     * @return lowercase string or empty string
     */
    private String toLowerOrEmpty(String value) {
        String result = "";
        if (value != null) {
            result = value.toLowerCase();
        }
        return result;
    }

    /**
     * Display the given list of emails in the IT dashboard table and
     * update the dashboard controller with the current emails.
     *
     * @param emails the emails to display
     */
    private void displayEmails(List<Email> emails) {
        final DefaultTableModel model =
                (DefaultTableModel) view.getItEmailTable().getModel();

        model.setRowCount(0);

        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Email email : emails) {
            final String status;
            if (email.getVerifiedStatus() != null) {
                status = email.getVerifiedStatus();
            }
            else {
                status = "Pending";
            }

            final String dateString;
            if (email.getDateReceived() != null) {
                dateString = email.getDateReceived().format(formatter);
            }
            else {
                dateString = "N/A";
            }

            model.addRow(new Object[] {
                    email.getId(),
                    email.getSender(),
                    email.getTitle(),
                    String.format("%.1f", email.getSuspicionScore()),
                    status,
                    dateString,
            });
        }

        dashboardController.setCurrentEmails(emails);
    }
}
