package interface_adapter.filter;

import data_access.FirebaseEmailDataAccessObject;
import entity.Email;
import interface_adapter.it_dashboard.ItDashboardController;
import view.ItDashboardView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItFilterController {

    private final ItDashboardView view;
    private final FirebaseEmailDataAccessObject emailDAO;
    private final ItDashboardController dashboardController;

    public ItFilterController(ItDashboardView view, ItDashboardController dashboardController) {
        this.view = view;
        this.emailDAO = new FirebaseEmailDataAccessObject();
        this.dashboardController = dashboardController;

        view.getItFilterButton().addActionListener(e -> applyFilter());
        view.getItDiscordButton().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Pretend this opens the Discord server!"));

        // Load initial data
        loadAllPinnedEmails();
    }

    private void loadAllPinnedEmails() {
        try {
            List<Email> emails = emailDAO.getPinnedEmails();
            displayEmails(emails);
        } catch (ExecutionException | InterruptedException e) {
            JOptionPane.showMessageDialog(view,
                    "Failed to load emails: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilter() {
        try {
            String keyword = view.getKeyword();
            String sender = view.getSender();
            String sortBy = view.getSort();

            // Get pinned emails
            List<Email> emails = emailDAO.getPinnedEmails();

            // Apply keyword filter
            if (!keyword.isEmpty()) {
                String lowerKeyword = keyword.toLowerCase();
                emails.removeIf(email ->
                        !email.getTitle().toLowerCase().contains(lowerKeyword) &&
                        !email.getSender().toLowerCase().contains(lowerKeyword) &&
                        !email.getBody().toLowerCase().contains(lowerKeyword)
                );
            }

            // Apply sender filter
            if (!sender.isEmpty()) {
                String lowerSender = sender.toLowerCase();
                emails.removeIf(email -> !email.getSender().toLowerCase().contains(lowerSender));
            }

            // Apply sorting
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
            }

            displayEmails(emails);
        } catch (ExecutionException | InterruptedException e) {
            JOptionPane.showMessageDialog(view,
                    "Failed to filter emails: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayEmails(List<Email> emails) {
        DefaultTableModel model = (DefaultTableModel) view.getItEmailTable().getModel();
        model.setRowCount(0);  // clear table

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Email email : emails) {
            String status = email.getVerifiedStatus() != null ? email.getVerifiedStatus() : "Pending";
            model.addRow(new Object[]{
                    email.getId(),
                    email.getSender(),
                    email.getTitle(),
                    String.format("%.1f", email.getSuspicionScore()),
                    status,
                    email.getDateReceived() != null ? email.getDateReceived().format(formatter) : "N/A"
            });
        }

        // Update the dashboard controller with current emails
        dashboardController.setCurrentEmails(emails);
    }
}
