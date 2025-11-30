// interface_adapter/it_dashboard/ItDashboardController.java
package interface_adapter.it_dashboard;

import entity.Email;
import interface_adapter.ViewManagerModel;
import use_case.it_dashboard_status.ItUpdateStatusInputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusInputData;
import view.ItDashboardView;
import view.EmailDecisionView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ItDashboardController {

    private final ItDashboardView itDashboardView;
    private final EmailDecisionView emailDecisionView;
    private final ViewManagerModel viewManagerModel;
    private final ItUpdateStatusInputBoundary updateStatusInteractor;

    private List<Email> currentEmails;

    public ItDashboardController(ItDashboardView itDashboardView,
                                 EmailDecisionView emailDecisionView,
                                 ViewManagerModel viewManagerModel,
                                 ItUpdateStatusInputBoundary updateStatusInteractor) {
        this.itDashboardView = itDashboardView;
        this.emailDecisionView = emailDecisionView;
        this.viewManagerModel = viewManagerModel;
        this.updateStatusInteractor = updateStatusInteractor;

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
        emailDecisionView.addConfirmListener(e -> updateEmailStatus("Confirmed"));
        emailDecisionView.addSafeListener(e -> updateEmailStatus("Safe"));
        emailDecisionView.addPendingListener(e -> updateEmailStatus("Pending"));
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

        // Find the email in the current list
        Email emailToUpdate = null;
        if (currentEmails != null) {
            for (Email email : currentEmails) {
                if (email.getId() == emailId) {
                    emailToUpdate = email;
                    break;
                }
            }
        }

        if (emailToUpdate == null || emailToUpdate.getDocumentId() == null) {
            JOptionPane.showMessageDialog(emailDecisionView,
                    "Email document ID not found",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Delegate to use case interactor
        ItUpdateStatusInputData inputData =
                new ItUpdateStatusInputData(emailToUpdate.getDocumentId(), status);
        updateStatusInteractor.updateStatus(inputData);
    }

    private void openDecisionScreen(int row) {
        JTable table = itDashboardView.getItEmailTable();

        int emailId = (Integer) table.getValueAt(row, 0);
        String sender = (String) table.getValueAt(row, 1);
        String title  = (String) table.getValueAt(row, 2);
        Object score  = table.getValueAt(row, 3);
        String status = (String) table.getValueAt(row, 4);
        String date   = table.getValueAt(row, 5).toString();

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
