package view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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
import javax.swing.table.DefaultTableModel;

/**
 * IT dashboard view displaying the phishing emails table and filters.
 */
public class ItDashboardView extends JPanel {

    private static final float TITLE_FONT_SIZE = 24f;
    private static final int GAP_SMALL = 5;
    private static final int PADDING = 10;

    private static final String DISCORD_INVITE_LINK_URL = "https://discord.gg/FmME2xh7";

    private final String viewName = "itdashboard";

    private JTable emailTable;
    private JTextField keywordField;
    private JTextField senderField;
    private JTextField minScoreField;
    private JTextField maxScoreField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;
    private JButton backButton;

    /**
     * Constructs the IT dashboard view.
     */
    public ItDashboardView() {
        setLayout(new BorderLayout());
        add(buildTitleLabel(), BorderLayout.NORTH);
        add(buildFilterPanel(), BorderLayout.WEST);
        add(buildTableScrollPane(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    private JLabel buildTitleLabel() {
        final JLabel title =
                new JLabel("Phishing Detection IT Dashboard", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(TITLE_FONT_SIZE));
        return title;
    }

    public void updateEmailStatusInTable(int emailId, String newStatus) {
        DefaultTableModel model = (DefaultTableModel) emailTable.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            // ID column
            Object value = model.getValueAt(row, 0);

            if (value instanceof Integer && ((Integer) value) == emailId) {
                // ⚠ column index 4 = "Status"
                model.setValueAt(newStatus, 4, row);
                // or model.setValueAt(newStatus, COLUMN_STATUS, row);
                break;
            }
        }
    }

    private JPanel buildFilterPanel() {
        final JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(0, 1, GAP_SMALL, GAP_SMALL));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING, PADDING, PADDING, PADDING));

        keywordField = new JTextField();
        senderField = new JTextField();
        minScoreField = new JTextField();
        maxScoreField = new JTextField();
        sortBox = new JComboBox<>(new String[] {"Title", "Date", "Sender", "Suspicion Score"});
        filterButton = new JButton("Apply Filter");
        backButton = new JButton("Back to Start");

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

        return filterPanel;
    }

    private JScrollPane buildTableScrollPane() {
        final String[] columns = {"ID", "Sender", "Title", "Suspicion Score", "Status", "Date"};

        final DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make table read-only
                return false;
            }
        };

        emailTable = new JTable(model);
        return new JScrollPane(emailTable);
    }

    private JPanel buildBottomPanel() {
        final JPanel bottomPanel = new JPanel();

        discordButton = new JButton("Join Discord Webhook");
        // Click button to open invite link to join Discord server in browser
        discordButton.addActionListener(event -> {
            try {
                Desktop.getDesktop().browse(new URI(DISCORD_INVITE_LINK_URL));
            }
            catch (IOException | URISyntaxException ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to open browser.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(discordButton);
        bottomPanel.add(backButton);
        return bottomPanel;
    }

    /**
     * Returns the view name used by the view manager.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Returns the filter button.
     *
     * @return the filter button
     */
    public JButton getItFilterButton() {
        return filterButton;
    }

    /**
     * Returns the Discord webhook button.
     *
     * @return the Discord webhook button
     */
    public JButton getItDiscordButton() {
        return discordButton;
    }

    /**
     * Returns the back button.
     *
     * @return the back button
     */
    public JButton getBackButton() {
        return backButton;
    }

    /**
     * Returns the email table.
     *
     * @return the email table
     */
    public JTable getItEmailTable() {
        return emailTable;
    }

    /**
     * Returns the keyword filter text.
     *
     * @return the keyword text
     */
    public String getKeyword() {
        return keywordField.getText();
    }

    /**
     * Returns the sender filter text.
     *
     * @return the sender text
     */
    public String getSender() {
        return senderField.getText();
    }

    /**
     * Returns the currently selected sort option.
     *
     * @return the sort criterion
     */
    public String getSort() {
        return (String) sortBox.getSelectedItem();
    }

    public void updateStatusAtRow(int rowIndex, String newStatus) {
        if (rowIndex < 0 || rowIndex >= emailTable.getRowCount()) {
            return; // out of bounds, ignore
        }
        emailTable.setValueAt(newStatus, 4, rowIndex);
        // 4 = "Status" column (ID, Sender, Title, Suspicion, Status, Date)
    }
}
