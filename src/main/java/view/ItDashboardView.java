package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;

public class ItDashboardView extends JPanel {
    private final String viewName = "itdashboard";

    private final JTable emailTable;
    private final JTextField keywordField;
    private final JTextField senderField;
    private final JTextField minScoreField;
    private final JTextField maxScoreField;
    private final JComboBox<String> sortBox;
    private final JButton filterButton;
    private final JButton discordButton;
    private JButton backButton;

    private final String discordInviteLinkURL = "https://discord.gg/FmME2xh7";

    public ItDashboardView() {
        super();
        setLayout(new BorderLayout());

        // Optional: add a title label at the top instead of using JFrame.setTitle(...)
        JLabel title = new JLabel("Phishing Detection IT Dashboard", SwingConstants.CENTER);
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
        sortBox = new JComboBox<>(new String[]{"Title", "Date", "Sender", "Suspicion Score"});
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

        add(filterPanel, BorderLayout.WEST);

        // ----- TABLE FOR PINNED EMAILS -----
        String[] columns = {"ID", "Sender", "Title", "Suspicion Score", "Status", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        emailTable = new JTable(model);

        // Add custom renderer for status column
//        emailTable.getColumnModel().getColumn(4).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
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

        JScrollPane scrollPane = new JScrollPane(emailTable);
        add(scrollPane, BorderLayout.CENTER);

        // ----- DISCORD BUTTON -----
        discordButton = new JButton("Join Discord Webhook");
        // Click button to open invite link to join Discord server in browser
        discordButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(discordInviteLinkURL));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Unable to open browser.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        backButton = new JButton("Back to Start");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getViewName() { return viewName;}

    // Expose widgets to controller
    public JButton getItFilterButton() { return filterButton; }
    public JButton getItDiscordButton() { return discordButton; }
    public JButton getBackButton() { return backButton; }
    public JTable getItEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }
}
