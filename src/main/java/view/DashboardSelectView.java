package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class DashboardSelectView extends JPanel {

    private static final String HTML_END = "</html>";

    private final String viewName = "dashboard-select";

    private final JTextArea emailArea = new JTextArea();
    private final JTextArea analysisArea = new JTextArea();
    private final JLabel senderLabel = new JLabel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel scoreLabel = new JLabel();
    private final JLabel statusLabel = new JLabel();
    private final JLabel dateLabel = new JLabel();

    private final JButton backButton = new JButton("Back to Dashboard");

    public DashboardSelectView() {
        setLayout(new BorderLayout());
        final int borderDimensions = 20;
        setBorder(new EmptyBorder(borderDimensions, borderDimensions, borderDimensions, borderDimensions));

        // ----- Title -----
        final JLabel title = new JLabel("View Email Details", SwingConstants.CENTER);
        final float titleFontSize = 26f;
        title.setFont(title.getFont().deriveFont(Font.BOLD, titleFontSize));
        add(title, BorderLayout.NORTH);

        // ----- Email Area -----
        emailArea.setEditable(false);
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        final int emailFontSize = 14;
        emailArea.setFont(new Font("Monospaced", Font.PLAIN, emailFontSize));

        final JScrollPane emailScroll = new JScrollPane(emailArea);
        emailScroll.setBorder(BorderFactory.createTitledBorder("Email Content"));

        // ----- Analysis Area -----
        analysisArea.setEditable(false);
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);
        final int analysisFontSize = 13;
        analysisArea.setFont(new Font("SansSerif", Font.PLAIN, analysisFontSize));
        // Light yellow background
        final int rValue = 255;
        final int gValue = 255;
        final int bValue = 240;
        analysisArea.setBackground(new Color(rValue, gValue, bValue));

        final JScrollPane analysisScroll = new JScrollPane(analysisArea);
        analysisScroll.setBorder(BorderFactory.createTitledBorder("Security Analysis"));
        // Fixed height
        final int scrollHeight = 250;
        analysisScroll.setPreferredSize(new Dimension(0, scrollHeight));

        // Split email content and analysis vertically using JSplitPane
        final JSplitPane splitPane =
                new JSplitPane(JSplitPane.VERTICAL_SPLIT, emailScroll, analysisScroll);
        // Give 60% to email, 40% to analysis
        final double resizeWidth = 0.6;
        final double dividerLocation = 0.6;
        splitPane.setResizeWeight(resizeWidth);
        splitPane.setDividerLocation(dividerLocation);

        // Put split pane + back button in a vertical column
        final JPanel leftColumn = new JPanel();
        final int hGap = 10;
        final int vGap = 10;
        leftColumn.setLayout(new BorderLayout(hGap, vGap));

        leftColumn.add(splitPane, BorderLayout.CENTER);

        final JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeft.add(backButton);

        leftColumn.add(bottomLeft, BorderLayout.SOUTH);

        add(leftColumn, BorderLayout.CENTER);

        // Right side panel for metadata
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        final int borderTop = 10;
        final int borderLeft = 20;
        final int borderBottom = 10;
        final int borderRight = 10;
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(borderTop, borderLeft, borderBottom, borderRight),
                BorderFactory.createTitledBorder("Email Information")
        ));
        // Set fixed width for info panel
        final int infoPanelWidth = 300;
        infoPanel.setPreferredSize(new Dimension(infoPanelWidth, 0));

        // Style labels
        final Font labelFont = new Font("SansSerif", Font.PLAIN, 14);
        senderLabel.setFont(labelFont);
        titleLabel.setFont(labelFont);
        scoreLabel.setFont(labelFont);
        statusLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);

        final int rigidAreaHeight1 = 10;
        final int rigidAreaHeight2 = 15;
        infoPanel.add(Box.createRigidArea(new Dimension(0, rigidAreaHeight1)));
        infoPanel.add(senderLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, rigidAreaHeight2)));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, rigidAreaHeight2)));
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, rigidAreaHeight2)));
        infoPanel.add(statusLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, rigidAreaHeight2)));
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createVerticalGlue());

        add(infoPanel, BorderLayout.EAST);
    }

    /**
     * Returns the name used for this view in the view manager.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Updates the right-hand metadata panel with the selected email's details.
     *
     * @param sender the email sender
     * @param title  the email title
     * @param score  the suspicion score
     * @param status the verification status
     * @param date   the date received
     */
    public void setEmailDetails(String sender, String title,
                                Object score, String status, String date) {
        senderLabel.setText("<html><b>Sender:</b><br/>" + sender + HTML_END);
        titleLabel.setText("<html><b>Title:</b><br/>" + title + HTML_END);
        scoreLabel.setText("<html><b>Suspicion Score:</b><br/>" + score + HTML_END);

        // Color code the status
        String statusColor = "black";
        if ("Confirmed".equals(status)) {
            statusColor = "#cc0000";
        }
        else if ("Safe".equals(status)) {
            statusColor = "#00aa00";
        }
        else if ("Pending".equals(status)) {
            statusColor = "#ccaa00";
        }

        statusLabel.setText(
                "<html><b>Status:</b><br/><span style='color:"
                        + statusColor + ";'>" + status + "</span>" + HTML_END
        );

        dateLabel.setText("<html><b>Date:</b><br/>" + date + HTML_END);
    }

    /**
     * Sets the main email body text.
     *
     * @param text the email content
     */
    public void setEmailText(String text) {
        emailArea.setText(text);
        emailArea.setCaretPosition(0);
    }

    /**
     * Sets the security analysis text.
     *
     * @param text the analysis content
     */
    public void setAnalysisText(String text) {
        analysisArea.setText(text);
        analysisArea.setCaretPosition(0);
    }

    /**
     * Adds a listener for the "Back to Dashboard" button.
     *
     * @param listener the listener to add
     */
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
