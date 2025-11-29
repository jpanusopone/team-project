package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardSelectView extends JPanel {
    private final String viewName = "dashboard-select";

    private final JTextArea emailArea = new JTextArea();
    private final JTextArea analysisArea = new JTextArea();
    private final JLabel senderLabel   = new JLabel();
    private final JLabel titleLabel    = new JLabel();
    private final JLabel scoreLabel    = new JLabel();
    private final JLabel statusLabel   = new JLabel();
    private final JLabel dateLabel     = new JLabel();

    private final JButton backButton = new JButton("Back to Dashboard");

    public DashboardSelectView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ----- Title -----
        JLabel title = new JLabel("View Email Details", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        add(title, BorderLayout.NORTH);

        // ----- Email Area -----
        emailArea.setEditable(false);
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        emailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane emailScroll = new JScrollPane(emailArea);
        emailScroll.setBorder(BorderFactory.createTitledBorder("Email Content"));

        // ----- Analysis Area -----
        analysisArea.setEditable(false);
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);
        analysisArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        analysisArea.setBackground(new Color(255, 255, 240)); // Light yellow background

        JScrollPane analysisScroll = new JScrollPane(analysisArea);
        analysisScroll.setBorder(BorderFactory.createTitledBorder("Security Analysis"));
        analysisScroll.setPreferredSize(new Dimension(0, 250)); // Fixed height

        // Split email content and analysis vertically using JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, emailScroll, analysisScroll);
        splitPane.setResizeWeight(0.6); // Give 60% to email, 40% to analysis
        splitPane.setDividerLocation(0.6);

        // Put split pane + back button in a vertical column
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BorderLayout(10, 10));

        leftColumn.add(splitPane, BorderLayout.CENTER);

        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeft.add(backButton);

        leftColumn.add(bottomLeft, BorderLayout.SOUTH);

        add(leftColumn, BorderLayout.CENTER);

        // Right side panel for metadata
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 20, 10, 10),
                BorderFactory.createTitledBorder("Email Information")
        ));
        infoPanel.setPreferredSize(new Dimension(300, 0)); // Set fixed width for info panel

        // Style labels
        Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        senderLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);
        scoreLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);

        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(senderLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalGlue());

        add(infoPanel, BorderLayout.EAST);
    }

    public String getViewName() {
        return viewName;
    }

    public void setEmailDetails(String sender, String title, Object score, String status, String date) {
        senderLabel.setText("<html><b>Sender:</b><br/>" + sender + "</html>");
        titleLabel.setText("<html><b>Title:</b><br/>" + title + "</html>");
        scoreLabel.setText("<html><b>Suspicion Score:</b><br/>" + score + "</html>");

        // Color code the status
        String statusColor = "black";
        if ("Confirmed".equals(status)) {
            statusColor = "#cc0000";  // Red
        } else if ("Safe".equals(status)) {
            statusColor = "#00aa00";  // Green
        } else if ("Pending".equals(status)) {
            statusColor = "#ccaa00";  // Yellow/Orange
        }
        statusLabel.setText("<html><b>Status:</b><br/><span style='color:" + statusColor + ";'>" + status + "</span></html>");

        dateLabel.setText("<html><b>Date:</b><br/>" + date + "</html>");
    }

    public void setEmailText(String text) {
        emailArea.setText(text);
        emailArea.setCaretPosition(0);
    }

    public void setAnalysisText(String text) {
        analysisArea.setText(text);
        analysisArea.setCaretPosition(0);
    }

    // Listener hook for AppBuilder or controller
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
