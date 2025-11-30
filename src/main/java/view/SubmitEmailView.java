package view;

import entity.Email;
import entity.EmailBuilder;
import entity.PhishingExplanation;
import entity.RiskLevel;
import interface_adapter.link_risk.LinkRiskController;
import interface_adapter.link_risk.LinkRiskState;
import interface_adapter.link_risk.LinkRiskViewModel;
import interface_adapter.save_email.SaveEmailController;
import interface_adapter.save_email.SaveEmailViewModel;
import presentation.ExplanationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Use Case 1 GUI: Submit email for analysis.
 */

public class SubmitEmailView extends JFrame implements PropertyChangeListener {
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
    private final JButton backToDashboardButton = new JButton("Back to Start");

    private final ExplanationController explanationController;
    private final SaveEmailController saveEmailController;
    private final SaveEmailViewModel saveEmailViewModel;
    private final LinkRiskController linkRiskController;
    private final LinkRiskViewModel linkRiskViewModel;
    private PhishingExplanation currentExplanation;

    public SubmitEmailView(ExplanationController controller, SaveEmailController saveController,
                           SaveEmailViewModel saveViewModel, LinkRiskController linkController,
                           LinkRiskViewModel linkViewModel) {
        super("Phish Detect - Submit Email");
        this.explanationController = controller;
        this.saveEmailController = saveController;
        this.saveEmailViewModel = saveViewModel;
        this.linkRiskController = linkController;
        this.linkRiskViewModel = linkViewModel;

        // Register as listener to view models
        this.saveEmailViewModel.addPropertyChangeListener(this);
        this.linkRiskViewModel.addPropertyChangeListener(this);

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

        // Parse date from field or use current time
        java.time.LocalDateTime dateReceived;
        try {
            dateReceived = java.time.LocalDateTime.parse(dateStr,
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            dateReceived = java.time.LocalDateTime.now();
        }

        java.util.List<String> links = currentExplanation.getIndicators() != null &&
                                        currentExplanation.getIndicators().getUrls() != null ?
                                        currentExplanation.getIndicators().getUrls() : new java.util.ArrayList<>();

        // Use controller to save email - follows clean architecture
        saveEmailController.execute(
                subject,
                sender,
                emailText,
                dateReceived,
                mapRiskLevelToScore(currentExplanation.getRiskLevel()),
                formatExplanation(currentExplanation),
                links
        );

        // Note: The actual result handling is done in propertyChange() method
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Handle save email result
        if ("saveResult".equals(evt.getPropertyName())) {
            pinButton.setEnabled(true);

            if (saveEmailViewModel.isSuccess()) {
                JOptionPane.showMessageDialog(
                        this,
                        saveEmailViewModel.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        saveEmailViewModel.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        // Handle link risk analysis result
        if ("state".equals(evt.getPropertyName()) && evt.getSource() == linkRiskViewModel) {
            LinkRiskState state = linkRiskViewModel.getState();

            if (state.getError() != null) {
                // API call failed, append error message
                String currentText = explanationArea.getText();
                String updatedText = currentText.replace(
                        "Link Analysis: Checking URLs with Google Safe Browsing...\n",
                        "Link Analysis: Unable to check URLs (" + state.getError() + ")\n"
                );
                explanationArea.setText(updatedText);
            } else if (state.getUrls() != null && state.getRiskLevels() != null) {
                // API call succeeded, update with real results
                StringBuilder linkAnalysis = new StringBuilder();
                linkAnalysis.append("Link Analysis (Google Safe Browsing):\n");

                boolean hasDangerousLinks = false;
                java.util.List<String> urls = state.getUrls();
                java.util.List<String> riskLevels = state.getRiskLevels();

                if (urls.isEmpty()) {
                    linkAnalysis.append("No links detected in email.\n");
                } else {
                    for (int i = 0; i < urls.size(); i++) {
                        String url = urls.get(i);
                        String risk = riskLevels.get(i);

                        if ("DANGEROUS".equals(risk)) {
                            linkAnalysis.append(" - ⚠️ ").append(url).append(" [DANGEROUS]\n");
                            hasDangerousLinks = true;
                        } else if ("SAFE".equals(risk)) {
                            linkAnalysis.append(" - ✓ ").append(url).append(" [SAFE]\n");
                        } else {
                            linkAnalysis.append(" - ? ").append(url).append(" [UNKNOWN]\n");
                        }
                    }
                }

                // Update warning label if dangerous links found
                if (hasDangerousLinks) {
                    warningLabel.setText("⚠️ DANGER: DO NOT CLICK ANY LINKS! ⚠️");
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setBackground(new Color(255, 220, 220));
                }

                // Replace loading message with actual results
                String currentText = explanationArea.getText();
                String updatedText = currentText.replace(
                        "Link Analysis: Checking URLs with Google Safe Browsing...\n",
                        linkAnalysis.toString()
                );
                explanationArea.setText(updatedText);
                explanationArea.setCaretPosition(0);
            }
        }
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

        // Update warning label based on suspicion
        if (explanation.isSuspicious()) {
            warningLabel.setText("!!! Be careful with links !!!");
            warningLabel.setForeground(Color.RED.darker());
            warningLabel.setBackground(new Color(255, 240, 240));
        } else {
            warningLabel.setText("No strong phishing signals, but stay cautious.");
            warningLabel.setForeground(new Color(0, 128, 0));
            warningLabel.setBackground(new Color(240, 255, 240));
        }

        // Build explanation text (without link analysis yet)
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
            sb.append("\n");
        }

        // Show loading message for link analysis
        sb.append("Link Analysis: Checking URLs with Google Safe Browsing...\n");

        explanationArea.setText(sb.toString());
        explanationArea.setCaretPosition(0);

        // Trigger async link risk analysis via API
        String emailText = emailArea.getText().trim();
        linkRiskController.execute(emailText);
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