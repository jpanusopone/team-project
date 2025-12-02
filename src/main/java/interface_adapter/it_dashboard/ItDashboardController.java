package interface_adapter.it_dashboard;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import entity.Email;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.ItFilterController;
import use_case.it_dashboard_status.ItUpdateStatusInputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusInputData;
import view.EmailDecisionView;
import view.ItDashboardView;

/**
 * Controller for the IT dashboard, handling table clicks and decision actions.
 */
public class ItDashboardController {

    private static final String NEWLINE = "\n";
    private static final String DOUBLE_NEWLINE = "\n\n";

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_SENDER = 1;
    private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_SCORE = 3;
    private static final int COLUMN_STATUS = 4;
    private static final int COLUMN_DATE = 5;

    private final ItDashboardView itDashboardView;
    private final EmailDecisionView emailDecisionView;
    private final ViewManagerModel viewManagerModel;
    private final ItUpdateStatusInputBoundary updateStatusInteractor;
    private ItFilterController filterController;

    private List<Email> currentEmails;

    private int lastSelectedRow = -1;

    /**
     * Constructs a controller for the IT dashboard.
     *
     * @param itDashboardView       the dashboard view
     * @param emailDecisionView     the decision view
     * @param viewManagerModel      the view manager model
     * @param updateStatusInteractor the interactor for updating status
     */
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

    /**
     * Set up listener for clicks on the email table.
     */
    private void setupTableListener() {
        final JTable table = itDashboardView.getItEmailTable();
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 1 && table.getSelectedRow() != -1) {
                    openDecisionScreen(table.getSelectedRow());
                }
            }
        });
    }

    /**
     * Set up listeners for the decision view buttons.
     */
    private void setupDecisionViewListeners() {
        emailDecisionView.addConfirmListener(actionEvent -> {
            updateEmailStatus("Confirmed");
        });
        emailDecisionView.addSafeListener(actionEvent -> {
            updateEmailStatus("Safe");
        });
        emailDecisionView.addPendingListener(actionEvent -> {
            updateEmailStatus("Pending");
        });
        emailDecisionView.addBackListener(actionEvent -> {
            viewManagerModel.setState(itDashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });
    }

    /**
     * Update the status of the currently selected email.
     *
     * @param status the new status
     */
    private void updateEmailStatus(String status) {
        final int emailId = emailDecisionView.getCurrentEmailId();
        String errorMessage = null;

        if (emailId == -1) {
            errorMessage = "No email selected";
        }
        else {
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
                errorMessage = "Email document ID not found";
            }
            else {
                ItUpdateStatusInputData inputData =
                        new ItUpdateStatusInputData(emailToUpdate.getDocumentId(), status);

                // 1) Update in Firebase (interactor)
                updateStatusInteractor.updateStatus(inputData);

                // 2) Update in our in-memory list
                emailToUpdate.setVerifiedStatus(status);

                // 3) Update the table row directly (no filtering)
                updateTableRowStatus(emailId, status);
            }
        }

        if (errorMessage != null) {
            JOptionPane.showMessageDialog(emailDecisionView,
                    errorMessage,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateTableRowStatus(int emailId, String status) {
        JTable table = itDashboardView.getItEmailTable();
        javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel) table.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            int idInRow = (Integer) model.getValueAt(row, COLUMN_ID);
            if (idInRow == emailId) {
                model.setValueAt(status, row, COLUMN_STATUS);
                break;
            }
        }
    }


    /**
     * Open the decision screen for the email in the given row.
     *
     * @param row the selected row index
     */
    private void openDecisionScreen(int row) {
        this.lastSelectedRow = row;

        final JTable table = itDashboardView.getItEmailTable();

        final int emailId = (Integer) table.getValueAt(row, COLUMN_ID);
        final String sender = (String) table.getValueAt(row, COLUMN_SENDER);
        final String title = (String) table.getValueAt(row, COLUMN_TITLE);
        final Object score = table.getValueAt(row, COLUMN_SCORE);
        final String status = (String) table.getValueAt(row, COLUMN_STATUS);
        final String date = table.getValueAt(row, COLUMN_DATE).toString();

        Email selectedEmail = null;
        if (currentEmails != null) {
            for (Email email : currentEmails) {
                if (email.getId() == emailId) {
                    selectedEmail = email;
                    break;
                }
            }
        }

        final StringBuilder emailText = new StringBuilder();
        emailText.append("Sender: ").append(sender).append(NEWLINE);
        emailText.append("Title: ").append(title).append(NEWLINE);
        emailText.append("Suspicion score: ").append(score).append(NEWLINE);
        emailText.append("Current Status: ").append(status).append(NEWLINE);
        emailText.append("Date: ").append(date).append(DOUBLE_NEWLINE);

        if (selectedEmail != null) {
            emailText.append("Body:").append(NEWLINE)
                    .append(selectedEmail.getBody())
                    .append(DOUBLE_NEWLINE);

            if (selectedEmail.getExplanation() != null) {
                emailText.append("Explanation:").append(NEWLINE)
                        .append(selectedEmail.getExplanation())
                        .append(DOUBLE_NEWLINE);
            }

            if (selectedEmail.getLinks() != null
                    && !selectedEmail.getLinks().isEmpty()) {
                emailText.append("Links:").append(NEWLINE);
                for (String link : selectedEmail.getLinks()) {
                    emailText.append("- ").append(link).append(NEWLINE);
                }
            }
        }

        emailDecisionView.setEmailText(emailText.toString());
        emailDecisionView.setCurrentEmailId(emailId);
        emailDecisionView.setCurrentRowIndex(row);

        viewManagerModel.setState(emailDecisionView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    /**
     * Set the current list of emails shown on the dashboard.
     *
     * @param emails the list of current emails
     */
    public void setCurrentEmails(List<Email> emails) {
        this.currentEmails = emails;
    }
}
