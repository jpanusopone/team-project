package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import java.awt.event.ActionListener;

public class StartView extends JPanel {
    private final String viewName = "start";

    private final JButton submitPhishingButton = new JButton("Submit Phishing Email");
    private final JButton dashboardButton = new JButton("Dashboard");
    private final JButton itLoginButton = new JButton("IT login");

    public StartView(){
        // Overall layout: title at top, buttons in center
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 40, 40, 40)); // padding around edges

        // ----- Title -----
        JLabel titleLabel = new JLabel("Phish Detect", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 32f));
        add(titleLabel, BorderLayout.NORTH);

        // ----- Buttons (stacked vertically, centered) -----
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBorder(new EmptyBorder(40, 0, 0, 0)); // space under title

        Dimension buttonSize = new Dimension(220, 40);

        addButtonToPanel(submitPhishingButton, buttonsPanel, buttonSize);
        addButtonToPanel(dashboardButton, buttonsPanel, buttonSize);
        addButtonToPanel(itLoginButton, buttonsPanel, buttonSize);

        add(buttonsPanel, BorderLayout.CENTER);
    }

    private void addButtonToPanel(JButton button, JPanel panel, Dimension size) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.setFocusPainted(false);
        button.setMargin(new Insets(5, 15, 5, 15));

        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // vertical gap
    }

    public String getViewName() { return viewName;}

    public JButton getSubmitPhishingButton() {
        return submitPhishingButton;
    }

    public JButton getDashboardButton() {
        return dashboardButton;
    }

    public JButton getItLoginButton() {
        return itLoginButton;
    }

    public void addSubmitPhishingListener(ActionListener listener) {
        submitPhishingButton.addActionListener(listener);
    }

    public void addDashboardListener(ActionListener listener) {
        dashboardButton.addActionListener(listener);
    }

    public void addItLoginListener(ActionListener listener) {
        itLoginButton.addActionListener(listener);
    }
}
