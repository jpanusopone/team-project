package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * The start view shown when the application launches.
 * Provides navigation to submitting an email, viewing the dashboard,
 * or opening the IT login.
 */
public class StartView extends JPanel {

    private static final int PADDING_TOP = 30;
    private static final int PADDING_LEFT = 40;
    private static final int PADDING_BOTTOM = 40;
    private static final int PADDING_RIGHT = 40;
    private static final int BUTTON_PANEL_TOP_GAP = 40;
    private static final int BUTTON_WIDTH = 220;
    private static final int BUTTON_HEIGHT = 40;
    private static final int BUTTON_VERTICAL_GAP = 20;
    private static final float TITLE_FONT_SIZE = 32f;

    private final String viewName = "start";

    private final JButton submitPhishingButton = new JButton("Submit Phishing Email");
    private final JButton dashboardButton = new JButton("Dashboard");
    private final JButton itLoginButton = new JButton("IT login");

    /**
     * Constructs the StartView with title and navigation buttons.
     */
    public StartView() {
        // Overall layout: title at top, buttons in center
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(
                PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT));

        // Title
        final JLabel titleLabel = new JLabel("Phish Detect", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(TITLE_FONT_SIZE));
        add(titleLabel, BorderLayout.NORTH);

        // Buttons (stacked vertically, centered)
        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(new EmptyBorder(BUTTON_PANEL_TOP_GAP, 0, 0, 0));

        final Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        addButtonToPanel(submitPhishingButton, buttonsPanel, buttonSize);
        addButtonToPanel(dashboardButton, buttonsPanel, buttonSize);
        addButtonToPanel(itLoginButton, buttonsPanel, buttonSize);

        add(buttonsPanel, BorderLayout.CENTER);
    }

    private void addButtonToPanel(JButton button, JPanel panel, Dimension size) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        // Vertical gap between buttons
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, BUTTON_VERTICAL_GAP)));
    }

    /**
     * Returns the logical name used for this view in the ViewManager.
     *
     * @return the view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Returns the button used to navigate to the submit phishing email view.
     *
     * @return the submit phishing button
     */
    public JButton getSubmitPhishingButton() {
        return submitPhishingButton;
    }

    /**
     * Returns the button used to navigate to the dashboard view.
     *
     * @return the dashboard button
     */
    public JButton getDashboardButton() {
        return dashboardButton;
    }

    /**
     * Returns the button used to navigate to the IT login view.
     *
     * @return the IT login button
     */
    public JButton getItLoginButton() {
        return itLoginButton;
    }

    /**
     * Adds a listener for the "Submit Phishing Email" button.
     *
     * @param listener the action listener to add
     */
    public void addSubmitPhishingListener(ActionListener listener) {
        submitPhishingButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the "Dashboard" button.
     *
     * @param listener the action listener to add
     */
    public void addDashboardListener(ActionListener listener) {
        dashboardButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the "IT login" button.
     *
     * @param listener the action listener to add
     */
    public void addItLoginListener(ActionListener listener) {
        itLoginButton.addActionListener(listener);
    }
}
