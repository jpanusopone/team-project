package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import entity.PhishingExplanation;
import entity.RiskLevel;
import interface_adapter.link_risk.LinkRiskController;
import interface_adapter.link_risk.LinkRiskState;
import interface_adapter.link_risk.LinkRiskViewModel;
import interface_adapter.save_email.SaveEmailController;
import interface_adapter.save_email.SaveEmailViewModel;
import presentation.ExplanationController;

/**
 * Use Case 1 GUI: Submit email for analysis.
 */
@SuppressWarnings({
        "checkstyle:ClassDataAbstractionCoupling",
        "checkstyle:ClassFanOutComplexity",
        "checkstyle:MagicNumber"
})
public class SubmitEmailView extends JFrame implements PropertyChangeListener {

    private static final float TITLE_FONT_SIZE = 18f;
    private static final float WARNING_FONT_SIZE = 14f;
    private static final float SCORE_FONT_SIZE = 64f;
    private static final float SCORE_TEXT_FONT_SIZE = 20f;
    private static final int GAP_DEFAULT = 10;
    private static final int PADDING_SMALL = 5;
    private static final int BUTTON_H_GAP = 20;
    private static final int BUTTON_V_GAP = 5;
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String LINK_ANALYSIS_LOADING =
            "Link Analysis: Checking URLs with Google Safe Browsing...\n";
    private static final String NEWLINE = "\n";

    private final JTextArea emailArea = new JTextArea(15, 40);

    // New fields for email metadata
    private final JTextField senderField = new JTextField(30);
    private final JTextField subjectField = new JTextField(30);
    private final JTextField dateField = new JTextField(30);

    private final JLabel warningLabel =
            new JLabel("Paste the email and click Submit", SwingConstants.CENTER);
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

    public SubmitEmailView(ExplanationController controller,
                           SaveEmailController saveController,
                           SaveEmailViewModel saveViewModel,
                           LinkRiskController linkController,
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
        setLayout(new BorderLayout(GAP_DEFAULT, GAP_DEFAULT));
        setResizable(true);

        add(buildTopTitleBar(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomButtons(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        attachListeners();
    }

    private Component buildTopTitleBar() {
        final JPanel bar = new JPanel(new BorderLayout());
        final JLabel title = new JLabel("Phish Detect", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        bar.add(title, BorderLayout.CENTER);
        bar.setBorder(BorderFactory.createEmptyBorder(
                PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL));
        return bar;
    }

    @SuppressWarnings("checkstyle:ExecutableStatementCount")
    private Component buildCenterPanel() {
        final JPanel center = new JPanel(new GridLayout(1, 2, GAP_DEFAULT, 0));

        // LEFT PANEL - Email input with metadata fields
        final JPanel leftPanel = new JPanel(new BorderLayout(PADDING_SMALL, PADDING_SMALL));

        // Metadata fields panel
        final JPanel metadataPanel = new JPanel(
                new GridLayout(3, 2, PADDING_SMALL, PADDING_SMALL));
        metadataPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL));

        metadataPanel.add(new JLabel("Sender:"));
        metadataPanel.add(senderField);
        metadataPanel.add(new JLabel("Subject:"));
        metadataPanel.add(subjectField);
        metadataPanel.add(new JLabel("Date:"));
        metadataPanel.add(dateField);

        // Set placeholder text
        dateField.setText(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern(DATE_PATTERN)));

        leftPanel.add(metadataPanel, BorderLayout.NORTH);

        // Email body area
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        final JScrollPane emailScroll = new JScrollPane(emailArea);
        final JPanel emailBodyPanel = new JPanel(new BorderLayout());
        emailBodyPanel.add(emailScroll, BorderLayout.CENTER);
        emailBodyPanel.setBorder(BorderFactory.createTitledBorder("Email Body"));

        leftPanel.add(emailBodyPanel, BorderLayout.CENTER);
        leftPanel.setBorder(BorderFactory.createTitledBorder("Email Information"));

        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(PADDING_SMALL, PADDING_SMALL));

        warningLabel.setOpaque(true);
        warningLabel.setBackground(new Color(255, 240, 240));
        warningLabel.setForeground(Color.RED.darker());
        warningLabel.setFont(warningLabel.getFont().deriveFont(Font.BOLD, WARNING_FONT_SIZE));
        warningLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        final JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scoreNumberLabel.setFont(
                scoreNumberLabel.getFont().deriveFont(Font.BOLD, SCORE_FONT_SIZE));
        scoreTextLabel.setFont(
                scoreTextLabel.getFont().deriveFont(Font.PLAIN, SCORE_TEXT_FONT_SIZE));
        scorePanel.add(Box.createVerticalStrut(GAP_DEFAULT));
        scorePanel.add(scoreNumberLabel);
        scorePanel.add(scoreTextLabel);
        scorePanel.add(Box.createVerticalStrut(GAP_DEFAULT));

        explanationArea.setEditable(false);
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        final JScrollPane explanationScroll = new JScrollPane(explanationArea);
        final JPanel explanationPanel = new JPanel(new BorderLayout());
        explanationPanel.add(explanationScroll, BorderLayout.CENTER);
        explanationPanel.setBorder(
                BorderFactory.createTitledBorder("Why this might be phishing"));

        rightPanel.add(warningLabel, BorderLayout.NORTH);
        rightPanel.add(scorePanel, BorderLayout.CENTER);
        rightPanel.add(explanationPanel, BorderLayout.SOUTH);

        center.add(leftPanel);
        center.add(rightPanel);

        center.setBorder(BorderFactory.createEmptyBorder(
                GAP_DEFAULT, GAP_DEFAULT, GAP_DEFAULT, GAP_DEFAULT));
        return center;
    }

    private Component buildBottomButtons() {
        final JPanel bottom =
                new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_H_GAP, BUTTON_V_GAP));
        bottom.add(submitButton);
        bottom.add(pinButton);
        bottom.add(backToDashboardButton);

        // Initially disable pin button until email is analyzed
        pinButton.setEnabled(false);

        return bottom;
    }

    private void attachListeners() {
        submitButton.addActionListener(event -> analyzeEmail());
        pinButton.addActionListener(event -> pinToDashboard());
    }

    /**
     * Allow external components to add a listener to the back to dashboard button.
     *
     * @param listener the action listener to attach
     */
    public void addBackToDashboardListener(ActionListener listener) {
        backToDashboardButton.addActionListener(listener);
    }

    private void analyzeEmail() {
        final String emailText = emailArea.getText().trim();

        if (isEmailPresent(emailText)) {
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

            final SwingWorker<PhishingExplanation, Void> worker =
                    new AnalyzeEmailWorker(emailText);
            worker.execute();
        }
    }

    private boolean isEmailPresent(String emailText) {
        boolean present = true;
        if (emailText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please paste an email before submitting.",
                    "No email",
                    JOptionPane.WARNING_MESSAGE
            );
            present = false;
        }
        return present;
    }

    private void pinToDashboard() {
        if (canPinCurrentEmail()) {
            final String emailText = emailArea.getText().trim();

            // Validate metadata fields
            final String sender = senderField.getText().trim();
            final String subject = subjectField.getText().trim();
            final String dateStr = dateField.getText().trim();

            pinButton.setEnabled(false);

            // Parse date from field or use current time
            LocalDateTime dateReceived;
            try {
                dateReceived = LocalDateTime.parse(
                        dateStr,
                        DateTimeFormatter.ofPattern(DATE_PATTERN));
            }
            catch (DateTimeParseException ex) {
                dateReceived = LocalDateTime.now();
            }

            final List<String> links = new ArrayList<>();
            if (currentExplanation.getIndicators() != null
                    && currentExplanation.getIndicators().getUrls() != null) {
                links.addAll(currentExplanation.getIndicators().getUrls());
            }

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
            // Result is handled in propertyChange()
        }
    }

    private boolean canPinCurrentEmail() {
        boolean canPin = true;

        if (currentExplanation == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please analyze an email first before pinning.",
                    "No Analysis",
                    JOptionPane.WARNING_MESSAGE
            );
            canPin = false;
        }

        final String emailText = emailArea.getText().trim();
        if (canPin && emailText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No email content to pin.",
                    "No Email",
                    JOptionPane.WARNING_MESSAGE
            );
            canPin = false;
        }

        final String sender = senderField.getText().trim();
        final String subject = subjectField.getText().trim();

        if (canPin && (sender.isEmpty() || subject.isEmpty())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in Sender and Subject fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            canPin = false;
        }

        return canPin;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Handle save email result
        if ("saveEmail".equals(evt.getPropertyName())) {
            pinButton.setEnabled(true);

            if (saveEmailViewModel.isSuccess()) {
                JOptionPane.showMessageDialog(
                        this,
                        saveEmailViewModel.getMessage(),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            else {
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
            final LinkRiskState state = linkRiskViewModel.getState();

            if (state.getError() != null) {
                // API call failed, append error message
                final String currentText = explanationArea.getText();
                final String updatedText = currentText.replace(
                        LINK_ANALYSIS_LOADING,
                        "Link Analysis: Unable to check URLs (" + state.getError() + ")\n"
                );
                explanationArea.setText(updatedText);
            }
            else if (state.getUrls() != null && state.getRiskLevels() != null) {
                // API call succeeded, update with real results
                final StringBuilder linkAnalysis = new StringBuilder();
                linkAnalysis.append("Link Analysis (Google Safe Browsing):\n");

                boolean hasDangerousLinks = false;
                final List<String> urls = state.getUrls();
                final List<String> riskLevels = state.getRiskLevels();

                if (urls.isEmpty()) {
                    linkAnalysis.append("No links detected in email.\n");
                }
                else {
                    for (int i = 0; i < urls.size(); i++) {
                        final String url = urls.get(i);
                        final String risk = riskLevels.get(i);

                        if ("DANGEROUS".equals(risk)) {
                            linkAnalysis.append(" - [DANGEROUS] ").append(url).append(NEWLINE);
                            hasDangerousLinks = true;
                        }
                        else if ("SAFE".equals(risk)) {
                            linkAnalysis.append(" - [SAFE] ").append(url).append(NEWLINE);
                        }
                        else {
                            linkAnalysis.append(" - [UNKNOWN] ").append(url).append(NEWLINE);
                        }
                    }
                }

                // Update warning label if dangerous links found
                if (hasDangerousLinks) {
                    warningLabel.setText("DANGER: DO NOT CLICK ANY LINKS!");
                    warningLabel.setForeground(Color.RED);
                    warningLabel.setBackground(new Color(255, 220, 220));
                }

                // Replace loading message with actual results
                final String currentText = explanationArea.getText();
                final String updatedText = currentText.replace(
                        LINK_ANALYSIS_LOADING,
                        linkAnalysis.toString()
                );
                explanationArea.setText(updatedText);
                explanationArea.setCaretPosition(0);
            }
        }
    }

    private String extractSender(String emailText) {
        String result = "Unknown Sender";
        final String[] lines = emailText.split(NEWLINE);
        boolean found = false;

        for (String line : lines) {
            if (!found && line.toLowerCase().startsWith("from:")) {
                result = line.substring(5).trim();
                found = true;
            }
        }

        if (!found) {
            final Pattern pattern = Pattern.compile(
                    "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
            final Matcher matcher = pattern.matcher(emailText);
            if (matcher.find()) {
                result = matcher.group();
            }
        }

        return result;
    }

    private String extractTitle(String emailText) {
        String result = "Untitled Email";
        final String[] lines = emailText.split(NEWLINE);
        boolean found = false;

        for (String line : lines) {
            if (!found && line.toLowerCase().startsWith("subject:")) {
                result = line.substring(8).trim();
                found = true;
            }
        }

        if (!found && lines.length > 0) {
            final String firstLine = lines[0];
            if (firstLine.length() > 50) {
                result = firstLine.substring(0, 50) + "...";
            }
            else {
                result = firstLine;
            }
        }

        return result;
    }

    private String formatExplanation(PhishingExplanation explanation) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Risk Level: ").append(explanation.getRiskLevel().name())
                .append(NEWLINE).append(NEWLINE);

        if (!explanation.getReasons().isEmpty()) {
            sb.append("Reasons:").append(NEWLINE);
            for (String reason : explanation.getReasons()) {
                sb.append("- ").append(reason).append(NEWLINE);
            }
            sb.append(NEWLINE);
        }

        if (!explanation.getSuggestedActions().isEmpty()) {
            sb.append("Suggested Actions:").append(NEWLINE);
            for (String action : explanation.getSuggestedActions()) {
                sb.append("- ").append(action).append(NEWLINE);
            }
        }

        return sb.toString();
    }

    private void updateUiWithExplanation(PhishingExplanation explanation) {
        final int score = mapRiskLevelToScore(explanation.getRiskLevel());
        scoreNumberLabel.setText(String.valueOf(score));

        // Update warning label based on suspicion
        if (explanation.isSuspicious()) {
            warningLabel.setText("Be careful with links.");
            warningLabel.setForeground(Color.RED.darker());
            warningLabel.setBackground(new Color(255, 240, 240));
        }
        else {
            warningLabel.setText("No strong phishing signals, but stay cautious.");
            warningLabel.setForeground(new Color(0, 128, 0));
            warningLabel.setBackground(new Color(240, 255, 240));
        }

        // Build explanation text (without link analysis yet)
        final StringBuilder sb = new StringBuilder();
        sb.append("Risk Level: ")
                .append(explanation.getRiskLevel().name().toLowerCase())
                .append(NEWLINE).append(NEWLINE);

        if (!explanation.getReasons().isEmpty()) {
            sb.append("Why this looks suspicious:").append(NEWLINE);
            for (String reason : explanation.getReasons()) {
                sb.append(" - ").append(reason).append(NEWLINE);
            }
            sb.append(NEWLINE);
        }

        if (!explanation.getSuggestedActions().isEmpty()) {
            sb.append("Suggested actions:").append(NEWLINE);
            for (String action : explanation.getSuggestedActions()) {
                sb.append(" - ").append(action).append(NEWLINE);
            }
            sb.append(NEWLINE);
        }

        // Show loading message for link analysis
        sb.append(LINK_ANALYSIS_LOADING);

        explanationArea.setText(sb.toString());
        explanationArea.setCaretPosition(0);

        // Trigger async link risk analysis via API
        final String emailText = emailArea.getText().trim();
        linkRiskController.execute(emailText);
    }

    private int mapRiskLevelToScore(RiskLevel level) {
        int score = 0;
        if (level == RiskLevel.HIGH) {
            score = 100;
        }
        else if (level == RiskLevel.MEDIUM) {
            score = 50;
        }
        else if (level == RiskLevel.LOW) {
            score = 20;
        }
        return score;
    }

    /**
     * Simple wrapper around the explanation controller call.
     *
     * @param emailText the email text to analyze
     * @return a wrapper containing success, explanation, and error message
     */
    private ExplanationResponseWrapper callExplanation(String emailText) {
        final var response = explanationController.getExplanation(emailText);
        return new ExplanationResponseWrapper(response.isSuccess(),
                response.getExplanation(), response.getErrorMessage());
    }

    /**
     * SwingWorker used to run phishing explanation in the background.
     */
    private final class AnalyzeEmailWorker extends SwingWorker<PhishingExplanation, Void> {

        private final String emailText;

        AnalyzeEmailWorker(String emailText) {
            this.emailText = emailText;
        }

        @Override
        protected PhishingExplanation doInBackground() {
            final ExplanationResponseWrapper wrapper = callExplanation(emailText);
            if (!wrapper.isSuccess()) {
                throw new ExplanationExceptionWrapper(wrapper.getErrorMessage());
            }
            return wrapper.getExplanation();
        }

        @Override
        protected void done() {
            submitButton.setEnabled(true);
            try {
                final PhishingExplanation explanation = get();
                currentExplanation = explanation;
                updateUiWithExplanation(explanation);
                // Enable pin button after successful analysis
                pinButton.setEnabled(true);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                warningLabel.setText("Error analyzing email.");
                warningLabel.setForeground(Color.RED);
                explanationArea.setText(
                        "Analysis was interrupted by the system.\n\n" + ex.getMessage());
                pinButton.setEnabled(false);
            }
            catch (ExecutionException ex) {
                warningLabel.setText("Error analyzing email.");
                warningLabel.setForeground(Color.RED);
                explanationArea.setText(
                        "An error occurred while contacting the analyzing service.\n\n"
                                + ex.getMessage());
                pinButton.setEnabled(false);
            }
        }
    }

    /**
     * Lightweight wrapper to avoid using the presentation class directly in the worker.
     */
    private static final class ExplanationResponseWrapper {

        private final boolean success;
        private final PhishingExplanation explanation;
        private final String errorMessage;

        ExplanationResponseWrapper(boolean success,
                                   PhishingExplanation explanation,
                                   String errorMessage) {
            this.success = success;
            this.explanation = explanation;
            this.errorMessage = errorMessage;
        }

        boolean isSuccess() {
            return success;
        }

        PhishingExplanation getExplanation() {
            return explanation;
        }

        String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Custom runtime wrapper to satisfy SwingWorker's checked exception mechanism.
     */
    private static final class ExplanationExceptionWrapper extends RuntimeException {

        ExplanationExceptionWrapper(String message) {
            super(message);
        }
    }
}
