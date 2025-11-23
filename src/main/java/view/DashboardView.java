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
import java.util.ArrayList;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DashboardView extends JPanel implements PropertyChangeListener{
    private final String viewName = "dashboard";
    private boolean userAppliedFilter = false;

    private FilterController filterController;
    private FilteredViewModel filteredViewModel;

    private JTable emailTable;
    private EmailTableModel emailTableModel;
    private JTextField keywordField;
    private JTextField senderField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;

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
        sortBox = new JComboBox<>(new String[]{"Title", "Sender", "Date Received", "Suspicion Score"});
        filterButton = new JButton("Apply Filter");

        filterButton.addActionListener(e -> onFilterButton());

        filterPanel.add(new JLabel("Keyword:"));
        filterPanel.add(keywordField);
        filterPanel.add(new JLabel("Sender:"));
        filterPanel.add(senderField);
        filterPanel.add(new JLabel("Sort by:"));
        filterPanel.add(sortBox);
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.WEST);

        // ----- TABLE FOR PINNED EMAILS -----
        emailTableModel = new EmailTableModel(new ArrayList<>()); // this will be a list of all pinned emails
        emailTable = new JTable(emailTableModel);

        JScrollPane scrollPane = new JScrollPane(emailTable);
        add(scrollPane, BorderLayout.CENTER);

        // ----- DISCORD BUTTON -----
        discordButton = new JButton("Join Discord Webhook");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public String getViewName() { return viewName;}

    // Expose widgets to controller
    public JButton getFilterButton() { return filterButton; }
    public JButton getDiscordButton() { return discordButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }

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

        if (evt.getPropertyName().equals("state")) {
            Object newValue = evt.getNewValue();

            if (newValue instanceof FilteredState) {
                FilteredState state = (FilteredState) newValue;
                List<Email> emails = state.getEmails();

                emailTableModel.setEmails(emails);

                if (emails.isEmpty() && userAppliedFilter) {
                    JOptionPane.showMessageDialog(
                            this,
                            "No emails matched your filter criteria.",
                            "No Results",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

                userAppliedFilter = false;

            }
        }
    }
    public void setFilteredViewModel(FilteredViewModel vm) {
        this.filteredViewModel = vm;
        vm.addPropertyChangeListener(this);
    }

}
