//package view;
//
//import entity.Email;
//import interface_adapter.filter.FilterController;
//import interface_adapter.filter.FilteredState;
//import interface_adapter.filter.FilteredViewModel;
//import interface_adapter.view_dashboard.DashboardState;
//import interface_adapter.view_dashboard.DashboardViewModel;
//import interface_adapter.view_dashboard.GetPinnedEmailsController;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.List;
//
//public class DashboardView extends JPanel implements PropertyChangeListener {
//    private final String viewName = "dashboard";
//
//    private JTable emailTable;
//    private JTextField keywordField;
//    private JTextField senderField;
//    private JTextField minScoreField;
//    private JTextField maxScoreField;
//    private JComboBox<String> sortBox;
//    private JButton filterButton;
//    private JButton discordButton;
//    private JButton backToStartButton;
//    private FilteredViewModel filteredViewModel;
//    private DashboardViewModel dashboardViewModel;
//    private GetPinnedEmailsController getPinnedEmailsController;
//    private List<Email> currentEmails; // Store current emails for row access
//
//    public DashboardView() {
//        super();
//        setLayout(new BorderLayout());
//
//        // Optional: add a title label at the top instead of using JFrame.setTitle(...)
//        JLabel title = new JLabel("Phishing Detection Dashboard", SwingConstants.CENTER);
//        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
//        add(title, BorderLayout.NORTH);
//
//        // ----- LEFT FILTER PANEL -----
//        JPanel filterPanel = new JPanel();
//        filterPanel.setLayout(new GridLayout(0,1,5,5));
//        filterPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
//
//        keywordField = new JTextField();
//        senderField = new JTextField();
//        minScoreField = new JTextField();
//        maxScoreField = new JTextField();
//        sortBox = new JComboBox<>(new String[]{"Date", "Sender", "Suspicion Score"});
//        filterButton = new JButton("Apply Filter");
//
//        filterPanel.add(new JLabel("Keyword:"));
//        filterPanel.add(keywordField);
//        filterPanel.add(new JLabel("Sender:"));
//        filterPanel.add(senderField);
//        filterPanel.add(new JLabel("Min Score:"));
//        filterPanel.add(minScoreField);
//        filterPanel.add(new JLabel("Max Score:"));
//        filterPanel.add(maxScoreField);
//        filterPanel.add(new JLabel("Sort by:"));
//        filterPanel.add(sortBox);
//        filterPanel.add(filterButton);
//
//        add(filterPanel, BorderLayout.WEST);
//
//        // ----- TABLE FOR PINNED EMAILS -----
//        String[] columns = {"Sender", "Title", "Suspicion Score", "Status", "Date"};
//        DefaultTableModel model = new DefaultTableModel(columns, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // Make table read-only
//            }
//        };
//        emailTable = new JTable(model);
//
//        // Add custom renderer for status column
//        emailTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
//            @Override
//            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
//                    boolean isSelected, boolean hasFocus, int row, int column) {
//                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//
//                if (!isSelected && value != null) {
//                    String status = value.toString();
//                    switch (status) {
//                        case "Confirmed":
//                            c.setBackground(new Color(255, 200, 200)); // Light red for confirmed phishing
//                            c.setForeground(Color.BLACK);
//                            break;
//                        case "Safe":
//                            c.setBackground(new Color(200, 255, 200)); // Light green for safe
//                            c.setForeground(Color.BLACK);
//                            break;
//                        case "Pending":
//                            c.setBackground(new Color(255, 255, 200)); // Light yellow for pending
//                            c.setForeground(Color.BLACK);
//                            break;
//                        default:
//                            c.setBackground(Color.WHITE);
//                            c.setForeground(Color.BLACK);
//                    }
//                } else if (isSelected) {
//                    c.setBackground(table.getSelectionBackground());
//                    c.setForeground(table.getSelectionForeground());
//                }
//
//                return c;
//            }
//        });
//
//        JScrollPane scrollPane = new JScrollPane(emailTable);
//        add(scrollPane, BorderLayout.CENTER);
//
//        // ----- BOTTOM BUTTONS -----
//        discordButton = new JButton("Join Discord Webhook");
//        backToStartButton = new JButton("Back to Start");
//        JPanel bottomPanel = new JPanel();
//        bottomPanel.add(discordButton);
//        bottomPanel.add(backToStartButton);
//        add(bottomPanel, BorderLayout.SOUTH);
//
//        setVisible(true);
//    }
//
//    /**
//     * Set the filtered view model and register as listener
//     */
//    public void setFilteredViewModel(FilteredViewModel viewModel) {
//        this.filteredViewModel = viewModel;
//        this.filteredViewModel.addPropertyChangeListener(this);
//    }
//
//    /**
//     * Set the dashboard view model and register as listener
//     */
//    public void setDashboardViewModel(DashboardViewModel viewModel) {
//        this.dashboardViewModel = viewModel;
//        this.dashboardViewModel.addPropertyChangeListener(this);
//    }
//
//    /**
//     * Set the controller for loading pinned emails
//     */
//    public void setGetPinnedEmailsController(GetPinnedEmailsController controller) {
//        this.getPinnedEmailsController = controller;
//    }
//
//    /**
//     * Load pinned emails from Firebase
//     */
//    public void loadPinnedEmails() {
//        if (getPinnedEmailsController != null) {
//            getPinnedEmailsController.execute();
//        }
//    }
//
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        Object newValue = evt.getNewValue();
//
//        // Handle filtered emails update (from filter use case)
//        if (newValue instanceof FilteredState) {
//            FilteredState state = (FilteredState) newValue;
//
//            if (state.getError() != null) {
//                // Show error message
//                JOptionPane.showMessageDialog(this, state.getError(), "Filter Error", JOptionPane.ERROR_MESSAGE);
//            } else if (state.getEmails() != null) {
//                // Update table with filtered emails
//                updateTable(state.getEmails());
//            }
//        }
//
//        // Handle dashboard state update (from get pinned emails use case)
//        if (newValue instanceof DashboardState) {
//            DashboardState state = (DashboardState) newValue;
//
//            if (state.getError() != null) {
//                // Show error message
//                JOptionPane.showMessageDialog(this, state.getError(), "Dashboard Error", JOptionPane.ERROR_MESSAGE);
//            } else if (state.getPinnedEmails() != null) {
//                // Update table with pinned emails
//                updateTable(state.getPinnedEmails());
//            }
//        }
//    }
//
//    /**
//     * Update the table with the list of emails
//     */
//    private void updateTable(List<Email> emails) {
//        this.currentEmails = emails; // Store emails for later access
//        DefaultTableModel model = (DefaultTableModel) emailTable.getModel();
//        model.setRowCount(0); // Clear existing rows
//
//        for (Email email : emails) {
//            String status = email.getVerifiedStatus() != null ? email.getVerifiedStatus() : "Pending";
//            model.addRow(new Object[]{
//                    email.getSender(),
//                    email.getTitle(),
//                    email.getSuspicionScore(),
//                    status,
//                    email.getDateReceived()
//            });
//        }
//    }
//
//    /**
//     * Get email at specific row index
//     */
//    public Email getEmailAtRow(int row) {
//        if (currentEmails != null && row >= 0 && row < currentEmails.size()) {
//            return currentEmails.get(row);
//        }
//        return null;
//    }
//
//    public String getViewName() { return viewName;}
//
//    // Expose widgets to controller
//    public JButton getFilterButton() { return filterButton; }
//    public JButton getDiscordButton() { return discordButton; }
//    public JButton getBackToStartButton() { return backToStartButton; }
//    public JTable getEmailTable() { return emailTable; }
//    public String getKeyword() { return keywordField.getText(); }
//    public String getSender() { return senderField.getText(); }
//    public String getMinScore() { return minScoreField.getText(); }
//    public String getMaxScore() { return maxScoreField.getText(); }
//    public String getSort() { return (String) sortBox.getSelectedItem(); }
//
//    public void addBackToStartListener(ActionListener listener) {
//        backToStartButton.addActionListener(listener);
//    }
//}

package view;

import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilteredState;
import interface_adapter.filter.FilteredViewModel;
import use_case.filter.SortBy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DashboardView extends JPanel implements PropertyChangeListener{
    private boolean userAppliedFilter = false;

    private FilterController filterController;

    private final JTable emailTable;
    private final EmailTableModel emailTableModel;
    private final JTextField keywordField;
    private final JTextField senderField;
    private final JTextField minScoreField;
    private final JTextField maxScoreField;
    private final JComboBox<String> sortBox;
    private final JButton filterButton;
    private final JButton discordButton;
    private final JButton backToStartButton;

    public DashboardView() {
        super();
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Phishing Detection Dashboard", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);

        // ----- LEFT FILTER PANEL -----
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(0,1,5,5));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        keywordField = new JTextField();
        senderField = new JTextField();
        minScoreField = new JTextField();
        maxScoreField = new JTextField();
        sortBox = new JComboBox<>(new String[]{"Title", "Sender", "Date Received", "Suspicion Score"});
        filterButton = new JButton("Apply Filter");

        filterButton.addActionListener(e -> {
            try {
                filterController.execute(
                        keywordField.getText(),
                        senderField.getText(),
                        minScoreField.getText(),
                        maxScoreField.getText(),
                        (String) sortBox.getSelectedItem()
                );
                userAppliedFilter = true;

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        filterPanel.add(new JLabel("Keyword:"));
        filterPanel.add(keywordField);
        filterPanel.add(new JLabel("Sender:"));
        filterPanel.add(senderField);
        filterPanel.add(new JLabel("Minimum Score:"));
        filterPanel.add(minScoreField);
        filterPanel.add(new JLabel("Maximum Score:"));
        filterPanel.add(maxScoreField);
        filterPanel.add(new JLabel("Sort by:"));
        filterPanel.add(sortBox);
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.WEST);

        // ----- TABLE FOR PINNED EMAILS -----
        emailTableModel = new EmailTableModel(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        emailTable = new JTable(emailTableModel);

        JScrollPane scrollPane = new JScrollPane(emailTable);
        add(scrollPane, BorderLayout.CENTER);

        // ----- DISCORD BUTTON AND BACK TO START BUTTON -----
        discordButton = new JButton("Join Discord Webhook");
        backToStartButton = new JButton("Back to Start");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        bottomPanel.add(backToStartButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public String getViewName() {
        return "dashboard";
    }

    // Expose widgets to controller
    public JButton getFilterButton() { return filterButton; }
    public JButton getDiscordButton() { return discordButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public Double getMinScore() { return Double.parseDouble(minScoreField.getText()); }
    public Double getMaxScore() { return Double.parseDouble(maxScoreField.getText()); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }

    public void setFilterController(FilterController controller) {
        this.filterController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        if (!evt.getPropertyName().equals("state")) return;
        FilteredState state = (FilteredState) evt.getNewValue();

        // 1. SHOW POPUP IF THERE'S AN ERROR
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // clear the error so it doesn't trigger again
            state.setError(null);
            return;
        }

        // 2. NORMAL FLOW: update table
        List<String> senders = state.getSenders();
        List<String> titles = state.getTitles();
        List<String> datesReceived = state.getDatesReceived();
        List<String> suspicionScores = state.getSuspicionScores();
        List<String> verifiedStatuses = state.getVerifiedStatuses();

        emailTableModel.setEmails(senders, titles, datesReceived, suspicionScores, verifiedStatuses);

        if (senders.isEmpty() && userAppliedFilter) {
            JOptionPane.showMessageDialog(
                    this,
                    "No emails matched your filter criteria.",
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }

        userAppliedFilter = false;
    }


    public void setFilteredViewModel(FilteredViewModel vm) {
        vm.addPropertyChangeListener(this);
    }

    public void addBackToStartListener(ActionListener listener) {
        backToStartButton.addActionListener(listener);
    }


}