package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilteredState;
import interface_adapter.filter.FilteredViewModel;

public class DashboardView extends JPanel implements PropertyChangeListener {

    private FilterController filterController;

    private boolean userAppliedFilter;

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

    private static final String DISCORD_INVITE_LINK_URL = "https://discord.gg/FmME2xh7";

    public DashboardView() {
        setLayout(new BorderLayout());

        final JLabel title = new JLabel("Phishing Detection Dashboard", SwingConstants.CENTER);
        final java.awt.Font baseFont = title.getFont();
        final float titleFontSize = 24f;
        title.setFont(baseFont.deriveFont(java.awt.Font.BOLD, titleFontSize));
        add(title, BorderLayout.NORTH);

        // ----- LEFT FILTER PANEL -----
        final JPanel filterPanel = new JPanel();
        final int filterHorGap = 5;
        final int filterVerGap = 5;
        filterPanel.setLayout(new GridLayout(0, 1, filterHorGap, filterVerGap));
        final int filterPanelBorder = 10;
        filterPanel.setBorder(BorderFactory.createEmptyBorder(filterPanelBorder, filterPanelBorder,
                filterPanelBorder, filterPanelBorder));

        keywordField = new JTextField();
        senderField = new JTextField();
        minScoreField = new JTextField();
        maxScoreField = new JTextField();
        sortBox = new JComboBox<>(new String[]{"Title", "Sender", "Date Received", "Suspicion Score"});
        filterButton = new JButton("Apply Filter");

        filterButton.addActionListener(event -> applyFilter());

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

        final JScrollPane scrollPane = new JScrollPane(emailTable);
        add(scrollPane, BorderLayout.CENTER);

        // ----- DISCORD BUTTON AND BACK TO START BUTTON -----
        discordButton = new JButton("Join Discord Server");

        // Click button to open invite link to join Discord server in browser
        discordButton.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(new URI(DISCORD_INVITE_LINK_URL));
            }
            catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to open browser.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backToStartButton = new JButton("Back to Start");
        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        bottomPanel.add(backToStartButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void applyFilter() {
        if (filterController == null) {
            return;
        }

        try {
            filterController.execute(
                    keywordField.getText(),
                    senderField.getText(),
                    minScoreField.getText(),
                    maxScoreField.getText(),
                    (String) sortBox.getSelectedItem()
            );
            userAppliedFilter = true;
        }
        catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Convenience for controllers / presenters
    public int getSelectedRow() {
        return emailTable.getSelectedRow();
    }

    // Updates Status for row
    public void updateStatusForRow(int rowIndex, String newStatus) {
        emailTableModel.setVerifiedStatusAtRow(rowIndex, newStatus);
    }

    /**
     * Called when this view is displayed to trigger an initial load.
     */
    public void onViewDisplayed() {
        if (filterController != null) {
            filterController.execute("", "", "0.0", "100.0", "Title");
        }
    }

    /**
     * Returns the view name used in the view manager.
     *
     * @return the view name
     */
    public String getViewName() {
        return "dashboard";
    }

    // Expose widgets to controller

    /**
     * Returns the filter button.
     *
     * @return the filter button
     */
    public JButton getFilterButton() {
        return filterButton;
    }

    /**
     * Returns the Discord button.
     *
     * @return the Discord button
     */
    public JButton getDiscordButton() {
        return discordButton;
    }

    /**
     * Returns the emails table.
     *
     * @return the email table
     */
    public JTable getEmailTable() {
        return emailTable;
    }

    /**
     * Returns the keyword entered in the filter field.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keywordField.getText();
    }

    /**
     * Returns the sender filter.
     *
     * @return the sender text
     */
    public String getSender() {
        return senderField.getText();
    }

    /**
     * Returns the minimum score as a double.
     *
     * @return minimum score
     */
    public Double getMinScore() {
        return Double.parseDouble(minScoreField.getText());
    }

    /**
     * Returns the maximum score as a double.
     *
     * @return maximum score
     */
    public Double getMaxScore() {
        return Double.parseDouble(maxScoreField.getText());
    }

    /**
     * Returns the selected sort option.
     *
     * @return sort option
     */
    public String getSort() {
        return (String) sortBox.getSelectedItem();
    }

    /**
     * Sets the filter controller used by this view.
     *
     * @param controller the filter controller
     */
    public void setFilterController(FilterController controller) {
        this.filterController = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        final FilteredState state = (FilteredState) evt.getNewValue();

        // 1. SHOW POPUP IF THERE'S AN ERROR
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );

            // clear the error so it does not trigger again
            state.setError(null);
        }
        else {
            // 2. NORMAL FLOW: update table
            final List<String> senders = state.getSenders();
            final List<String> titles = state.getTitles();
            final List<String> datesReceived = state.getDatesReceived();
            final List<String> suspicionScores = state.getSuspicionScores();
            final List<String> verifiedStatuses = state.getVerifiedStatuses();

            emailTableModel.setEmails(
                    senders, titles, datesReceived, suspicionScores, verifiedStatuses
            );

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
    }

    /**
     * Registers this view as a listener to the filtered view model.
     *
     * @param viewModel the filtered view model
     */
    public void setFilteredViewModel(FilteredViewModel viewModel) {
        viewModel.addPropertyChangeListener(this);
    }

    /**
     * Adds a listener to the back-to-start button.
     *
     * @param listener the listener to add
     */
    public void addBackToStartListener(ActionListener listener) {
        backToStartButton.addActionListener(listener);
    }
}
