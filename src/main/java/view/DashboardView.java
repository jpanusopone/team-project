package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardView extends JFrame {

    private JTable emailTable;
    private JTextField keywordField;
    private JTextField senderField;
    private JComboBox<String> sortBox;
    private JButton filterButton;
    private JButton discordButton;

    public DashboardView() {
        setTitle("Phishing Detection Dashboard");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        setVisible(true);
    }

    // Expose widgets to controller
    public JButton getFilterButton() { return filterButton; }
    public JButton getDiscordButton() { return discordButton; }
    public JTable getEmailTable() { return emailTable; }
    public String getKeyword() { return keywordField.getText(); }
    public String getSender() { return senderField.getText(); }
    public String getSort() { return (String) sortBox.getSelectedItem(); }
}

