package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class EmailDecisionView extends JPanel {

    private final String viewName = "email-decision";

    private final JTextArea emailArea = new JTextArea();
    private final JButton confirmButton = new JButton("Confirm Phishing");
    private final JButton safeButton    = new JButton("Mark Safe");
    private final JButton pendingButton = new JButton("Pending");
    private final JButton backButton    = new JButton("Back");

    private int currentEmailId = -1;

    public EmailDecisionView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ----- Title -----
        JLabel title = new JLabel("Phish Detect - IT", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        add(title, BorderLayout.NORTH);

        // ----- Left: email text area -----
        emailArea.setEditable(false);
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        emailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane emailScroll = new JScrollPane(emailArea);
        emailScroll.setBorder(BorderFactory.createTitledBorder("Email"));

        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.add(emailScroll, BorderLayout.CENTER);
        emailPanel.setBorder(new EmptyBorder(10, 0, 10, 20));

        add(emailPanel, BorderLayout.CENTER);

        // ----- Right: vertical button column -----
        JPanel buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn, BoxLayout.Y_AXIS));
        buttonColumn.setBorder(new EmptyBorder(40, 20, 40, 0));

        Dimension buttonSize = new Dimension(180, 45);
        styleButton(confirmButton, buttonSize);
        styleButton(safeButton, buttonSize);
        styleButton(pendingButton, buttonSize);

        buttonColumn.add(confirmButton);
        buttonColumn.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonColumn.add(safeButton);
        buttonColumn.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonColumn.add(pendingButton);
        buttonColumn.add(Box.createRigidArea(new Dimension(0, 100))); // extra space before back
        buttonColumn.add(backButton);

        add(buttonColumn, BorderLayout.EAST);
    }

    private void styleButton(JButton button, Dimension size) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.setFocusPainted(false);
    }

    public String getViewName() {
        return viewName;
    }

    // Called by controller to show the clicked email
    public void setEmailText(String text) {
        emailArea.setText(text);
        emailArea.setCaretPosition(0);
    }

    public void setCurrentEmailId(int emailId) {
        this.currentEmailId = emailId;
    }

    public int getCurrentEmailId() {
        return currentEmailId;
    }

    // Listener hooks for controller
    public void addConfirmListener(ActionListener l) { confirmButton.addActionListener(l); }
    public void addSafeListener(ActionListener l)    { safeButton.addActionListener(l); }
    public void addPendingListener(ActionListener l) { pendingButton.addActionListener(l); }
    public void addBackListener(ActionListener l)    { backButton.addActionListener(l); }
}
