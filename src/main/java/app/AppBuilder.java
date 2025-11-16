package app;

import config.ApplicationConfig;
import presentation.ExplanationController;
import presentation.ExplanationResponse;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private JFrame frame;
    private JTextArea emailInputArea;
    private JTextArea explanationOutputArea;
    private JButton analyzeButton;
    private JLabel statusLabel;
    private ExplanationController controller;

    public AppBuilder() {
        controller = ApplicationConfig.createExplanationController();
        initializeUI();
    }

    private void initializeUI() {
        // Create main frame
        frame = new JFrame("Phishing Email Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout(10, 10));

        // Create top panel with title
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(52, 73, 94));
        JLabel titleLabel = new JLabel("AI-Powered Phishing Email Analyzer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Create center panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);

        // Email input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel inputLabel = new JLabel("Email Content (paste suspicious email here):");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(inputLabel, BorderLayout.NORTH);

        emailInputArea = new JTextArea();
        emailInputArea.setLineWrap(true);
        emailInputArea.setWrapStyleWord(true);
        emailInputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        emailInputArea.setText("Paste your email content here...");
        JScrollPane inputScrollPane = new JScrollPane(emailInputArea);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        // Explanation output panel
        JPanel outputPanel = new JPanel(new BorderLayout(5, 5));
        outputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JLabel outputLabel = new JLabel("AI Analysis Result:");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        outputPanel.add(outputLabel, BorderLayout.NORTH);

        explanationOutputArea = new JTextArea();
        explanationOutputArea.setLineWrap(true);
        explanationOutputArea.setWrapStyleWord(true);
        explanationOutputArea.setEditable(false);
        explanationOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        explanationOutputArea.setBackground(new Color(245, 245, 245));
        explanationOutputArea.setText("Analysis will appear here...");
        JScrollPane outputScrollPane = new JScrollPane(explanationOutputArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);

        splitPane.setTopComponent(inputPanel);
        splitPane.setBottomComponent(outputPanel);
        frame.add(splitPane, BorderLayout.CENTER);

        // Create bottom panel with button and status
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        analyzeButton = new JButton("Analyze Email");
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 14));
        analyzeButton.setBackground(new Color(46, 204, 113));
        analyzeButton.setForeground(Color.WHITE);
        analyzeButton.setFocusPainted(false);
        analyzeButton.setPreferredSize(new Dimension(150, 40));
        analyzeButton.addActionListener(e -> analyzeEmail());

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(analyzeButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Center the frame on screen
        frame.setLocationRelativeTo(null);
    }

    private void analyzeEmail() {
        String emailContent = emailInputArea.getText().trim();

        if (emailContent.isEmpty() || emailContent.equals("Paste your email content here...")) {
            JOptionPane.showMessageDialog(frame,
                "Please enter email content to analyze.",
                "Input Required",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Disable button and show processing status
        analyzeButton.setEnabled(false);
        statusLabel.setText("Analyzing email with AI... (using ChatGPT, fallback to DeepSeek/Claude if needed)");
        explanationOutputArea.setText("Processing...");

        // Run analysis in background thread to keep UI responsive
        SwingWorker<ExplanationResponse, Void> worker = new SwingWorker<ExplanationResponse, Void>() {
            @Override
            protected ExplanationResponse doInBackground() {
                return controller.getExplanation(emailContent);
            }

            @Override
            protected void done() {
                try {
                    ExplanationResponse response = get();
                    displayResult(response);
                } catch (Exception ex) {
                    explanationOutputArea.setText("Error: " + ex.getMessage());
                    statusLabel.setText("Error occurred");
                } finally {
                    analyzeButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void displayResult(ExplanationResponse response) {
        if (response.isSuccess()) {
            var explanation = response.getExplanation();
            StringBuilder result = new StringBuilder();

            result.append("=== ANALYSIS RESULT ===\n\n");

            // Suspicious status
            result.append("Status: ").append(explanation.isSuspicious() ? "SUSPICIOUS \u26A0" : "SAFE \u2713").append("\n");
            result.append("Risk Level: ").append(explanation.getRiskLevel()).append("\n\n");

            // Reasons
            result.append("--- Reasons ---\n");
            for (String reason : explanation.getReasons()) {
                result.append("  \u2022 ").append(reason).append("\n");
            }
            result.append("\n");

            // Indicators
            var indicators = explanation.getIndicators();
            result.append("--- Phishing Indicators ---\n");

            if (indicators.getSender() != null) {
                result.append("  Sender: ").append(indicators.getSender()).append("\n");
            }
            if (indicators.getReplyTo() != null) {
                result.append("  Reply-To: ").append(indicators.getReplyTo()).append("\n");
            }
            if (indicators.getDisplayNameMismatch() != null) {
                result.append("  Display Name Mismatch: ").append(indicators.getDisplayNameMismatch() ? "Yes" : "No").append("\n");
            }
            if (indicators.getUrgentLanguage() != null) {
                result.append("  Urgent Language: ").append(indicators.getUrgentLanguage() ? "Yes" : "No").append("\n");
            }
            if (indicators.getRequestsSensitiveInfo() != null) {
                result.append("  Requests Sensitive Info: ").append(indicators.getRequestsSensitiveInfo() ? "Yes" : "No").append("\n");
            }
            if (!indicators.getUrls().isEmpty()) {
                result.append("  URLs Found:\n");
                for (String url : indicators.getUrls()) {
                    result.append("    - ").append(url).append("\n");
                }
            }
            if (!indicators.getAttachments().isEmpty()) {
                result.append("  Attachments:\n");
                for (String attachment : indicators.getAttachments()) {
                    result.append("    - ").append(attachment).append("\n");
                }
            }
            result.append("\n");

            // Suggested actions
            result.append("--- Suggested Actions ---\n");
            for (String action : explanation.getSuggestedActions()) {
                result.append("  \u2022 ").append(action).append("\n");
            }

            explanationOutputArea.setText(result.toString());
            statusLabel.setText("Analysis complete");
        } else {
            explanationOutputArea.setText("ERROR: " + response.getErrorMessage());
            statusLabel.setText("Analysis failed");
        }
    }

    public void show() {
        frame.setVisible(true);
    }
}
