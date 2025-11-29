package view;

import entity.Email;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilteredState;
import interface_adapter.filter.FilteredViewModel;
import use_case.filter.SortBy;

import javax.swing.*;
import java.awt.*;
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

            // clear the error so it doesn’t trigger again
            state.setError(null);
            return;
        }

        // 2. NORMAL FLOW: update table
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


    public void setFilteredViewModel(FilteredViewModel vm) {
        vm.addPropertyChangeListener(this);
    }

}
