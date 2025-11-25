package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class EmailDecisionView extends JPanel {

    private final String viewName = "email-decision";

    private final JLabel senderLabel   = new JLabel();
    private final JLabel titleLabel    = new JLabel();
    private final JLabel scoreLabel    = new JLabel();
    private final JLabel dateLabel     = new JLabel();

    private final JButton confirmButton = new JButton("Confirm Phishing");
    private final JButton safeButton    = new JButton("Mark Safe");
    private final JButton pendingButton = new JButton("Pending");

    public EmailDecisionView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 40, 40, 40));

        JLabel title = new JLabel("Review Email", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);

        // Info panel (sender, title, etc.)
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.add(senderLabel);
        infoPanel.add(titleLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(dateLabel);
        add(infoPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);
        buttonPanel.add(safeButton);
        buttonPanel.add(pendingButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    // Called by controller when the user clicks a row
    public void setEmailDetails(String sender, String title, Object score, String date) {
        senderLabel.setText("Sender: " + sender);
        titleLabel.setText("Title: " + title);
        scoreLabel.setText("Suspicion score: " + score);
        dateLabel.setText("Date: " + date);
    }

    // Listener hooks for the buttons
    public void addConfirmListener(ActionListener l) { confirmButton.addActionListener(l); }
    public void addSafeListener(ActionListener l)    { safeButton.addActionListener(l); }
    public void addPendingListener(ActionListener l) { pendingButton.addActionListener(l); }
}