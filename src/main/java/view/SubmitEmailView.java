package view;

import config.ApplicationConfig;
import entity.PhishingExplanation;
import entity.RiskLevel;
import presentation.ExplanationController;

import javax.swing.*;
import java.awt.*;

/**
 * Use Case 1 GUI: Submit email for analysis.
 */

public class SubmitEmailView extends JFrame{
    private final JTextArea emailArea = new JTextArea(18, 40);

    private final JLabel warningLabel = new JLabel("Paste the email and click Submit", SwingConstants.CENTER);
    private final JLabel scoreNumberLabel = new JLabel("--", SwingConstants.CENTER);
    private final JLabel scoreTextLabel = new JLabel("Phishing score", SwingConstants.CENTER);
    private final JTextArea explanationArea = new JTextArea(8, 30);

    private final JButton submitButton = new JButton("Submit");
    private final JButton pinButton = new JButton("Pin to Dashboard");

    private final ExplanationController explanationController;

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

        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        JScrollPane emailScroll = new JScrollPane(emailArea);
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(emailScroll, BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Email"));

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
        return bottom;
    }

    private void attachListeners(){
        submitButton.addActionListener(e -> analyzeEmail());
        pinButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Pin To Dashboard",
                    "Pinned to dashboard (stub). This will be handled by Use Case 2.",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
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
                    updateUIWithExplanation(explanation);
                } catch (Exception ex) {
                    warningLabel.setText("Error analyzing email.");
                    warningLabel.setForeground(Color.RED);
                    explanationArea.setText("An error occurred while contacting the analyzing service.\n\n"
                        + ex.getMessage());
                }
            }
        };
        worker.execute();
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
        return switch (level) {
            case HIGH -> 100;
            case MEDIUM -> 50;
            case LOW -> 20;
        };
    }
}