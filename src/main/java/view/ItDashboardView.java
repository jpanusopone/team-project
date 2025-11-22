package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ItDashboardView extends JPanel {
    private final String viewName = "itdashboard";

    private JTable emailTable;
    private JTextField keywordField;
    private JTextField senderField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;

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

        // ----- DISCORD BUTTON -----
        discordButton = new JButton("Join Discord Webhook");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(discordButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getViewName() { return viewName;}

    // Expose widgets to controller
    public JButton getItFilterButton() { return filterButton; }
    public JButton getItDiscordButton() { return discordButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }
}

