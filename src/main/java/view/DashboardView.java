package view;

import entity.Email;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilteredState;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.view_dashboard.DashboardState;
import interface_adapter.view_dashboard.DashboardViewModel;
import interface_adapter.view_dashboard.GetPinnedEmailsController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DashboardView extends JPanel implements PropertyChangeListener {
    private final String viewName = "dashboard";

    private JTable emailTable;
    private JTextField keywordField;
    private JTextField senderField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;
    private JButton backToStartButton;
    private FilteredViewModel filteredViewModel;
    private DashboardViewModel dashboardViewModel;
    private GetPinnedEmailsController getPinnedEmailsController;

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
        sortBox = new JComboBox<>(new String[]{"Date", "Sender", "Suspicion Score"});
        filterButton = new JButton("Apply Filter");

        filterPanel.add(new JLabel("Keyword:"));
        filterPanel.add(keywordField);
        filterPanel.add(new JLabel("Sender:"));
        filterPanel.add(senderField);
        filterPanel.add(new JLabel("Sort by:"));
        filterPanel.add(sortBox);
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.WEST);

        // ----- TABLE FOR PINNED EMAILS -----
        String[] columns = {"Sender", "Title", "Suspicion Score", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        emailTable = new JTable(model);

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

    /**
     * Set the controller for loading pinned emails
     */
    public void setGetPinnedEmailsController(GetPinnedEmailsController controller) {
        this.getPinnedEmailsController = controller;
    }

    /**
     * Load pinned emails from Firebase
     */
    public void loadPinnedEmails() {
        if (getPinnedEmailsController != null) {
            getPinnedEmailsController.execute();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();

        // Handle filtered emails update (from filter use case)
        if (newValue instanceof FilteredState) {
            FilteredState state = (FilteredState) newValue;

            if (state.getError() != null) {
                // Show error message
                JOptionPane.showMessageDialog(this, state.getError(), "Filter Error", JOptionPane.ERROR_MESSAGE);
            } else if (state.getEmails() != null) {
                // Update table with filtered emails
                updateTable(state.getEmails());
            }
        }

        // Handle dashboard state update (from get pinned emails use case)
        if (newValue instanceof DashboardState) {
            DashboardState state = (DashboardState) newValue;

            if (state.getError() != null) {
                // Show error message
                JOptionPane.showMessageDialog(this, state.getError(), "Dashboard Error", JOptionPane.ERROR_MESSAGE);
            } else if (state.getPinnedEmails() != null) {
                // Update table with pinned emails
                updateTable(state.getPinnedEmails());
            }
        }
    }

    /**
     * Update the table with the list of emails
     */
    private void updateTable(List<Email> emails) {
        DefaultTableModel model = (DefaultTableModel) emailTable.getModel();
        model.setRowCount(0); // Clear existing rows

        for (Email email : emails) {
            model.addRow(new Object[]{
                    email.getSender(),
                    email.getTitle(),
                    email.getSuspicionScore(),
                    email.getDateReceived()
            });
        }
    }

    public String getViewName() { return viewName;}

    // Expose widgets to controller
    public JButton getFilterButton() { return filterButton; }
    public JButton getDiscordButton() { return discordButton; }
    public JButton getBackToStartButton() { return backToStartButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }

    public void addBackToStartListener(ActionListener listener) {
        backToStartButton.addActionListener(listener);
    }
}
