package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * View used by IT to inspect a single email and mark its status.
 */
public class EmailDecisionView extends JPanel {

    private static final int BORDER_PADDING = 20;
    private static final int EMAIL_PANEL_TOP = 10;
    private static final int EMAIL_PANEL_BOTTOM = 10;
    private static final int EMAIL_PANEL_RIGHT = 20;
    private static final int BUTTON_COLUMN_TOP = 40;
    private static final int BUTTON_COLUMN_BOTTOM = 40;
    private static final int BUTTON_COLUMN_LEFT = 20;
    private static final int BUTTON_COLUMN_RIGHT = 0;
    private static final int BUTTON_VERTICAL_GAP = 20;
    private static final int EXTRA_BACK_BUTTON_GAP = 100;
    private static final float TITLE_FONT_SIZE = 26f;
    private static final float EMAIL_FONT_SIZE = 14f;

    private final String viewName = "email-decision";

    private final JTextArea emailArea = new JTextArea();
    private final JButton confirmButton = new JButton("Confirm Phishing");
    private final JButton safeButton = new JButton("Mark Safe");
    private final JButton pendingButton = new JButton("Pending");
    private final JButton backButton = new JButton("Back");

    private int currentEmailId = -1;

    /**
     * Constructs the email decision view.
     */
    public EmailDecisionView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(
                BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

        add(buildTitleLabel(), BorderLayout.NORTH);
        add(buildEmailPanel(), BorderLayout.CENTER);
        add(buildButtonColumn(), BorderLayout.EAST);
    }

    private JLabel buildTitleLabel() {
        final JLabel title = new JLabel("Phish Detect - IT", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(TITLE_FONT_SIZE));
        return title;
    }

    private JPanel buildEmailPanel() {
        emailArea.setEditable(false);
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        emailArea.setFont(emailArea.getFont().deriveFont(EMAIL_FONT_SIZE));

        final JScrollPane emailScroll = new JScrollPane(emailArea);
        emailScroll.setBorder(BorderFactory.createTitledBorder("Email"));

        final JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.add(emailScroll, BorderLayout.CENTER);
        emailPanel.setBorder(BorderFactory.createEmptyBorder(
                EMAIL_PANEL_TOP, 0, EMAIL_PANEL_BOTTOM, EMAIL_PANEL_RIGHT));

        return emailPanel;
    }

    private JPanel buildButtonColumn() {
        final JPanel buttonColumn = new JPanel();
        buttonColumn.setLayout(new BoxLayout(buttonColumn, BoxLayout.Y_AXIS));
        buttonColumn.setBorder(BorderFactory.createEmptyBorder(
                BUTTON_COLUMN_TOP, BUTTON_COLUMN_LEFT,
                BUTTON_COLUMN_BOTTOM, BUTTON_COLUMN_RIGHT));

        styleButton(confirmButton);
        styleButton(safeButton);
        styleButton(pendingButton);

        buttonColumn.add(confirmButton);
        buttonColumn.add(Box.createVerticalStrut(BUTTON_VERTICAL_GAP));
        buttonColumn.add(safeButton);
        buttonColumn.add(Box.createVerticalStrut(BUTTON_VERTICAL_GAP));
        buttonColumn.add(pendingButton);

        // Extra space before the back button
        buttonColumn.add(Box.createVerticalStrut(EXTRA_BACK_BUTTON_GAP));
        buttonColumn.add(backButton);

        return buttonColumn;
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
    }

    /**
     * Returns the view name used by the view manager.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Sets the email text to display and scrolls to the top.
     *
     * @param text the email body text
     */
    public void setEmailText(String text) {
        emailArea.setText(text);
        emailArea.setCaretPosition(0);
    }

    /**
     * Sets the identifier of the currently displayed email.
     *
     * @param emailId the email identifier
     */
    public void setCurrentEmailId(int emailId) {
        this.currentEmailId = emailId;
    }

    /**
     * Returns the identifier of the currently displayed email.
     *
     * @return the email identifier, or -1 if none is selected
     */
    public int getCurrentEmailId() {
        return currentEmailId;
    }

    /**
     * Adds a listener for the "Confirm Phishing" button.
     *
     * @param listener the action listener to add
     */
    public void addConfirmListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the "Mark Safe" button.
     *
     * @param listener the action listener to add
     */
    public void addSafeListener(ActionListener listener) {
        safeButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the "Pending" button.
     *
     * @param listener the action listener to add
     */
    public void addPendingListener(ActionListener listener) {
        pendingButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the "Back" button.
     *
     * @param listener the action listener to add
     */
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
