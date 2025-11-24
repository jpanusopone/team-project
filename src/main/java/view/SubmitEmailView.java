package view;

import config.ApplicationConfig;
import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import presentation.ExplanationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Use Case 1 GUI: Submit email for analysis.
 */

public class SubmitEmailView extends JFrame{
    private final JTextArea emailArea = new JTextArea(15, 40);

    // New fields for email metadata
    private final JTextField senderField = new JTextField(30);
    private final JTextField subjectField = new JTextField(30);
    private final JTextField dateField = new JTextField(30);

    private final JLabel warningLabel = new JLabel("Paste the email and click Submit", SwingConstants.CENTER);
    private final JLabel scoreNumberLabel = new JLabel("--", SwingConstants.CENTER);
    private final JLabel scoreTextLabel = new JLabel("Phishing score", SwingConstants.CENTER);
    private final JTextArea explanationArea = new JTextArea(8, 30);

    private final JButton submitButton = new JButton("Submit");
    private final JButton pinButton = new JButton("Pin to Dashboard");
    private final JButton backToDashboardButton = new JButton("Back to Dashboard");

    private final ExplanationController explanationController;
    private PhishingExplanation currentExplanation;

    public SubmitEmailView() {
        this(ApplicationConfig.createExplanationController());
    }

    public SubmitEmailView(ExplanationController controller) {
        super("Phish Detect - Submit Email");
        this.explanationController = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setResizable(true);

        add(buildTopTitleBar(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomButtons(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        attachListeners();
    }

    private JComponent buildTopTitleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Phish Detect", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        bar.add(title, BorderLayout.CENTER);
        bar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return bar;
    }

    private JComponent buildCenterPanel() {
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));

        // LEFT PANEL - Email input with metadata fields
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));

        // Metadata fields panel
        JPanel metadataPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        metadataPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        metadataPanel.add(new JLabel("Sender:"));
        metadataPanel.add(senderField);
        metadataPanel.add(new JLabel("Subject:"));
        metadataPanel.add(subjectField);
        metadataPanel.add(new JLabel("Date:"));
        metadataPanel.add(dateField);

        // Set placeholder text
        dateField.setText(java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));

        leftPanel.add(metadataPanel, BorderLayout.NORTH);

        // Email body area
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        JScrollPane emailScroll = new JScrollPane(emailArea);
        JPanel emailBodyPanel = new JPanel(new BorderLayout());
        emailBodyPanel.add(emailScroll, BorderLayout.CENTER);
        emailBodyPanel.setBorder(BorderFactory.createTitledBorder("Email Body"));

        leftPanel.add(emailBodyPanel, BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Email Information"));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(5,5));

        warningLabel.setOpaque(true);
        warningLabel.setBackground(new Color(255, 240, 240));
        warningLabel.setForeground(Color.RED.darker());
        warningLabel.setFont(warningLabel.getFont().deriveFont(Font.BOLD, 14f));
        warningLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scoreNumberLabel.setFont(scoreNumberLabel.getFont().deriveFont(Font.BOLD, 64f));
        scoreTextLabel.setFont(scoreTextLabel.getFont().deriveFont(Font.PLAIN, 20f));
        scorePanel.add(Box.createVerticalStrut(10));
        scorePanel.add(scoreNumberLabel);
        scorePanel.add(scoreTextLabel);
        scorePanel.add(Box.createVerticalStrut(10));

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        JScrollPane explanationScroll = new JScrollPane(explanationArea);
        JPanel explanationPanel = new JPanel(new BorderLayout());
        explanationPanel.add(explanationScroll, BorderLayout.CENTER);
        explanationPanel.setBorder(BorderFactory.createTitledBorder("Why this might be phishing"));

        rightPanel.add(warningLabel, BorderLayout.NORTH);
        rightPanel.add(scorePanel, BorderLayout.CENTER);
        rightPanel.add(explanationPanel, BorderLayout.SOUTH);

        center.add(leftPanel, BorderLayout.WEST);
        center.add(rightPanel, BorderLayout.EAST);

        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return center;
    }

    private JComponent buildBottomButtons() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        bottom.add(submitButton);
        bottom.add(pinButton);
        bottom.add(backToDashboardButton);

        // Initially disable pin button until email is analyzed
        pinButton.setEnabled(false);

        return bottom;
    }

    private void attachListeners(){
        submitButton.addActionListener(e -> analyzeEmail());
        pinButton.addActionListener(e -> pinToDashboard());
    }

    /**
     * Allow external components to add a listener to the back to dashboard button
     */
    public void addBackToDashboardListener(ActionListener listener) {
        backToDashboardButton.addActionListener(listener);
    }

    private void analyzeEmail() {
        String emailText = emailArea.getText().trim();
        if (emailText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please paste an email before submitting.",
                    "No email",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Auto-populate metadata fields if they're empty
        if (senderField.getText().trim().isEmpty()) {
            senderField.setText(extractSender(emailText));
        }
        if (subjectField.getText().trim().isEmpty()) {
            subjectField.setText(extractTitle(emailText));
        }

        submitButton.setEnabled(false);
        warningLabel.setText("Analyzing...");
        warningLabel.setForeground(Color.DARK_GRAY);
        explanationArea.setText("");
        scoreNumberLabel.setText("--");

        SwingWorker<PhishingExplanation, Void> worker = new SwingWorker<>() {
            @Override
            protected PhishingExplanation doInBackground() throws Exception{
                var response = explanationController.getExplanation(emailText);

                if (!response.isSuccess()) {
                    throw new Exception(response.getErrorMessage());
                }
                return response.getExplanation();
            }

            @Override
            protected void done() {
                submitButton.setEnabled(true);
                try {
                    PhishingExplanation explanation = get();
                    currentExplanation = explanation;
                    updateUIWithExplanation(explanation);
                    // Enable pin button after successful analysis
                    pinButton.setEnabled(true);
                } catch (Exception ex) {
                    warningLabel.setText("Error analyzing email.");
                    warningLabel.setForeground(Color.RED);
                    explanationArea.setText("An error occurred while contacting the analyzing service.\n\n"
                        + ex.getMessage());
                    pinButton.setEnabled(false);
                }
            }
        };
        worker.execute();
    }

    private void pinToDashboard() {
        if (currentExplanation == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please analyze an email first before pinning.",
                    "No Analysis",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String emailText = emailArea.getText().trim();
        if (emailText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No email content to pin.",
                    "No Email",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate metadata fields
        String sender = senderField.getText().trim();
        String subject = subjectField.getText().trim();
        String dateStr = dateField.getText().trim();

        if (sender.isEmpty() || subject.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in Sender and Subject fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        pinButton.setEnabled(false);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Parse date from field or use current time
                java.time.LocalDateTime dateReceived;
                try {
                    dateReceived = java.time.LocalDateTime.parse(dateStr,
                        java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    // If parsing fails, use current time
                    dateReceived = java.time.LocalDateTime.now();
                }

                // Create email entity
                EmailBuilder builder = new EmailBuilder();
                Email email = builder
                        .title(subject)
                        .sender(sender)
                        .body(emailText)
                        .dateReceived(dateReceived)
                        .suspicionScore(mapRiskLevelToScore(currentExplanation.getRiskLevel()))
                        .explanation(formatExplanation(currentExplanation))
                        .links(currentExplanation.getIndicators() != null &&
                               currentExplanation.getIndicators().getUrls() != null ?
                               currentExplanation.getIndicators().getUrls() : new java.util.ArrayList<>())
                        .pinned(true)
                        .pinnedDate(java.time.LocalDateTime.now())
                        .verifiedStatus("Pending")
                        .build();

                // Save to Firebase
                data_access.FirebaseEmailDataAccessObject emailDAO = new data_access.FirebaseEmailDataAccessObject();
                emailDAO.saveEmail(email);

                return null;
            }

            @Override
            protected void done() {
                pinButton.setEnabled(true);
                try {
                    get();
                    JOptionPane.showMessageDialog(
                            SubmitEmailView.this,
                            "Email successfully pinned to dashboard!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            SubmitEmailView.this,
                            "Failed to pin email: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }

    private String extractSender(String emailText) {
        // Simple extraction - look for "From:" or email pattern
        String[] lines = emailText.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().startsWith("from:")) {
                return line.substring(5).trim();
            }
        }
        // Try to find email pattern
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        java.util.regex.Matcher matcher = pattern.matcher(emailText);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Unknown Sender";
    }

    private String extractTitle(String emailText) {
        // Look for "Subject:" line
        String[] lines = emailText.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().startsWith("subject:")) {
                return line.substring(8).trim();
            }
        }
        // Use first line if no subject found
        if (lines.length > 0) {
            return lines[0].length() > 50 ? lines[0].substring(0, 50) + "..." : lines[0];
        }
        return "Untitled Email";
    }

    private String formatExplanation(PhishingExplanation explanation) {
        StringBuilder sb = new StringBuilder();
        sb.append("Risk Level: ").append(explanation.getRiskLevel().name()).append("\n\n");

        if (!explanation.getReasons().isEmpty()) {
            sb.append("Reasons:\n");
            for (String reason : explanation.getReasons()) {
                sb.append("- ").append(reason).append("\n");
            }
            sb.append("\n");
        }

        if (!explanation.getSuggestedActions().isEmpty()) {
            sb.append("Suggested Actions:\n");
            for (String action : explanation.getSuggestedActions()) {
                sb.append("- ").append(action).append("\n");
            }
        }

        return sb.toString();
    }

    private void updateUIWithExplanation(PhishingExplanation explanation) {
        int score = mapRiskLevelToScore(explanation.getRiskLevel());
        scoreNumberLabel.setText(String.valueOf(score));

        if (explanation.isSuspicious()) {
            warningLabel.setText("!!! Be careful with links !!!");
            warningLabel.setForeground(Color.RED.darker());
        } else {
            warningLabel.setText("No strong phishing signals, but stay cautious.");
            warningLabel.setForeground(new Color(0, 128, 0));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Risk Level: ").append(explanation.getRiskLevel().name().toLowerCase()).append("\n\n");

        if (!explanation.getReasons().isEmpty()) {
            sb.append("Why this looks suspicious:\n");
            for (String r : explanation.getReasons()) {
                sb.append(" - ").append(r).append("\n");
            }
            sb.append("\n");
        }

        if (!explanation.getSuggestedActions().isEmpty()) {
            sb.append("Suggested actions:\n");
            for (String a : explanation.getSuggestedActions()) {
                sb.append(" - ").append(a).append("\n");
            }
        }

        explanationArea.setText(sb.toString());
        explanationArea.setCaretPosition(0);
    }

    private int mapRiskLevelToScore(RiskLevel level) {
        switch (level) {
            case HIGH:
                return 100;
            case MEDIUM:
                return 50;
            case LOW:
                return 20;
            default:
                return 0;
        }
    }
}