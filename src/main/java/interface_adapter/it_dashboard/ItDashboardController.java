package interface_adapter.it_dashboard;

import data_access.FirebaseEmailDataAccessObject;
import entity.Email;
import interface_adapter.ViewManagerModel;
import view.ItDashboardView;
import view.EmailDecisionView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItDashboardController {

    private final ItDashboardView itDashboardView;
    private final EmailDecisionView emailDecisionView;
    private final ViewManagerModel viewManagerModel;
    private final FirebaseEmailDataAccessObject emailDAO;
    private List<Email> currentEmails;

    public ItDashboardController(ItDashboardView itDashboardView,
                                 EmailDecisionView emailDecisionView,
                                 ViewManagerModel viewManagerModel) {
        this.itDashboardView = itDashboardView;
        this.emailDecisionView = emailDecisionView;
        this.viewManagerModel = viewManagerModel;
        this.emailDAO = new FirebaseEmailDataAccessObject();

        setupTableListener();
        setupDecisionViewListeners();
    }

    private void setupTableListener() {
        JTable table = itDashboardView.getItEmailTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    openDecisionScreen(table.getSelectedRow());
                }
            }
        });
    }

    private void setupDecisionViewListeners() {
        emailDecisionView.addConfirmListener(e -> {
            updateEmailStatus("Confirmed");
        });

        emailDecisionView.addSafeListener(e -> {
            updateEmailStatus("Safe");
        });

        emailDecisionView.addPendingListener(e -> {
            updateEmailStatus("Pending");
        });

        emailDecisionView.addBackListener(e -> {
            viewManagerModel.setState(itDashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });
    }

    private void updateEmailStatus(String status) {
        int emailId = emailDecisionView.getCurrentEmailId();
        if (emailId == -1) {
            JOptionPane.showMessageDialog(emailDecisionView,
                    "No email selected",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Find the email in the current list
            Email emailToUpdate = null;
            for (Email email : currentEmails) {
                if (email.getId() == emailId) {
                    emailToUpdate = email;
                    break;
                }
            }

            if (emailToUpdate != null && emailToUpdate.getDocumentId() != null) {
                // Update in Firebase using the document ID
                emailDAO.updateVerificationStatus(emailToUpdate.getDocumentId(), status);

                JOptionPane.showMessageDialog(emailDecisionView,
                        "Email status updated to: " + status,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                // Go back to dashboard
                viewManagerModel.setState(itDashboardView.getViewName());
                viewManagerModel.firePropertyChange();
            } else {
                JOptionPane.showMessageDialog(emailDecisionView,
                        "Email document ID not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (ExecutionException | InterruptedException ex) {
            JOptionPane.showMessageDialog(emailDecisionView,
                    "Failed to update email status: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDecisionScreen(int row) {
        JTable table = itDashboardView.getItEmailTable();

        int emailId = (Integer) table.getValueAt(row, 0);
        String sender = (String) table.getValueAt(row, 1);
        String title  = (String) table.getValueAt(row, 2);
        Object score  = table.getValueAt(row, 3);
        String status = (String) table.getValueAt(row, 4);
        String date   = table.getValueAt(row, 5).toString();

        // Find the full email from current list
        Email selectedEmail = null;
        if (currentEmails != null) {
            for (Email email : currentEmails) {
                if (email.getId() == emailId) {
                    selectedEmail = email;
                    break;
                }
            }
        }

        StringBuilder emailText = new StringBuilder();
        emailText.append("Sender: ").append(sender).append("\n");
        emailText.append("Title: ").append(title).append("\n");
        emailText.append("Suspicion score: ").append(score).append("\n");
        emailText.append("Current Status: ").append(status).append("\n");
        emailText.append("Date: ").append(date).append("\n\n");

        if (selectedEmail != null) {
            emailText.append("Body:\n").append(selectedEmail.getBody()).append("\n\n");
            if (selectedEmail.getExplanation() != null) {
                emailText.append("Explanation:\n").append(selectedEmail.getExplanation()).append("\n\n");
            }
            if (selectedEmail.getLinks() != null && !selectedEmail.getLinks().isEmpty()) {
                emailText.append("Links:\n");
                for (String link : selectedEmail.getLinks()) {
                    emailText.append("- ").append(link).append("\n");
                }
            }
        }

        emailDecisionView.setEmailText(emailText.toString());
        emailDecisionView.setCurrentEmailId(emailId);

        viewManagerModel.setState(emailDecisionView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    public void setCurrentEmails(List<Email> emails) {
        this.currentEmails = emails;
    }
}
