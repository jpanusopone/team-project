package view;

import entity.Email;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilteredState;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.view_dashboard.DashboardState;
import interface_adapter.view_dashboard.DashboardViewModel;
import interface_adapter.view_dashboard.EmailTableModel;
import use_case.filter.SortBy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DashboardView extends JPanel implements PropertyChangeListener{
    private final String viewName = "dashboard";
    private boolean userAppliedFilter = false;

    private FilterController filterController;
    private FilteredViewModel filteredViewModel;
    private DashboardViewModel dashboardViewModel;

    private JTable emailTable;
    private EmailTableModel emailTableModel;
    private JTextField keywordField;
    private JTextField senderField;
    private JTextField minScoreField;
    private JTextField maxScoreField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;
    private JButton backToStartButton;
    private List<Email> currentEmails; // Store current emails for row access

    public DashboardView() {
        super();
        setLayout(new BorderLayout());

        // Optional: add a title label at the top instead of using JFrame.setTitle(...)
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

        filterButton.addActionListener(e -> onFilterButton());

        filterPanel.add(new JLabel("Keyword:"));
        filterPanel.add(keywordField);
        filterPanel.add(new JLabel("Sender:"));
        filterPanel.add(senderField);
        filterPanel.add(new JLabel("Min Score:"));
        filterPanel.add(minScoreField);
        filterPanel.add(new JLabel("Max Score:"));
        filterPanel.add(maxScoreField);
        filterPanel.add(new JLabel("Sort by:"));
        filterPanel.add(sortBox);
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.WEST);

        // ----- TABLE FOR PINNED EMAILS -----
        emailTableModel = new EmailTableModel(new ArrayList<>()); // this will be a list of all pinned emails
        emailTable = new JTable(emailTableModel);

        // Add custom renderer for status column
        emailTable.getColumnModel().getColumn(3).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected && value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Confirmed":
                            c.setBackground(new Color(255, 200, 200)); // Light red for confirmed phishing
                            c.setForeground(Color.BLACK);
                            break;
                        case "Safe":
                            c.setBackground(new Color(200, 255, 200)); // Light green for safe
                            c.setForeground(Color.BLACK);
                            break;
                        case "Pending":
                            c.setBackground(new Color(255, 255, 200)); // Light yellow for pending
                            c.setForeground(Color.BLACK);
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                } else if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(emailTable);
        add(scrollPane, BorderLayout.CENTER);

        // ----- BOTTOM BUTTONS -----
        discordButton = new JButton("Join Discord Webhook");
        backToStartButton = new JButton("Back to Start");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        bottomPanel.add(backToStartButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Set the filtered view model and register as listener
     */
    public void setFilteredViewModel(FilteredViewModel viewModel) {
        this.filteredViewModel = viewModel;
        this.filteredViewModel.addPropertyChangeListener(this);
    }

    /**
     * Set the dashboard view model and register as listener
     */
    public void setDashboardViewModel(DashboardViewModel viewModel) {
        this.dashboardViewModel = viewModel;
        this.dashboardViewModel.addPropertyChangeListener(this);
    }

    public String getViewName() { return viewName;}

    // Expose widgets to controller
    public JButton getFilterButton() { return filterButton; }
    public JButton getDiscordButton() { return discordButton; }
    public JButton getBackToStartButton() { return backToStartButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getMinScore() { return minScoreField.getText(); }
    public String getMaxScore() { return maxScoreField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }

    public void addBackToStartListener(ActionListener listener) {
        backToStartButton.addActionListener(listener);
    }

    public void setFilterController(FilterController controller) {
        this.filterController = controller;
    }

//    public void setPinnedEmailsController(GetPinnedEmailsController controller) {this.pinnedEmailsController = controller; }

    private void onFilterButton() {
        String keyword = keywordField.getText();
        String sender = senderField.getText();
        String sortValue = (String) sortBox.getSelectedItem();

        SortBy sortBy;

        switch (sortValue) {
            case "Title":
                sortBy = SortBy.TITLE;
                break;
            case "Sender":
                sortBy = SortBy.SENDER;
                break;
            case "Date Received":
                sortBy = SortBy.DATE_RECEIVED;
                break;
            case "Suspicion Score":
                sortBy = SortBy.SUSPICION_SCORE;
                break;
            default:
                sortBy = null;
                break;
        }

        userAppliedFilter = true;

        filterController.execute(keyword, sender, sortBy);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("PropertyChange received: " + evt.getPropertyName());

        if (evt.getPropertyName().equals("state")) {
            Object newValue = evt.getNewValue();

            if (newValue instanceof FilteredState) {
                FilteredState state = (FilteredState) newValue;
                List<Email> emails = state.getEmails();
                System.out.println("Filtered emails count: " + emails.size());

                emailTableModel.setEmails(emails);

                if (emails.isEmpty() && userAppliedFilter) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No emails match your filter criteria.",
                            "No Results",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

                userAppliedFilter = false;

            } else if (newValue instanceof DashboardState) {
                DashboardState state = (DashboardState) newValue;
                List<Email> emails = state.getEmails();
                System.out.println("Dashboard emails count: " + emails.size());
                emailTableModel.setEmails(emails);
            }
        }
    }
}
