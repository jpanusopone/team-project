package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class DashboardSelectView extends JPanel {
    private final String viewName = "dashboard-select";

    private final JTextArea emailArea = new JTextArea();
    private final JLabel senderLabel   = new JLabel();
    private final JLabel titleLabel    = new JLabel();
    private final JLabel scoreLabel    = new JLabel();
    private final JLabel dateLabel     = new JLabel();

    private final JButton backButton = new JButton("Back");

    public DashboardSelectView() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ----- Title -----
        JLabel title = new JLabel("View Email", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        add(title, BorderLayout.NORTH);

        // ----- Email Area -----
        emailArea.setEditable(false);
        emailArea.setLineWrap(true);
        emailArea.setWrapStyleWord(true);
        emailArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane emailScroll = new JScrollPane(emailArea);
        emailScroll.setBorder(BorderFactory.createTitledBorder("Email"));

        // Put email + back button in a vertical column
        JPanel leftColumn = new JPanel();
        leftColumn.setLayout(new BorderLayout(10, 10));

        leftColumn.add(emailScroll, BorderLayout.CENTER);

        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeft.add(backButton);

        leftColumn.add(bottomLeft, BorderLayout.SOUTH);

        add(leftColumn, BorderLayout.CENTER);

        // (Optional) panel on right side for metadata or future use
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(40, 20, 40, 0));

        infoPanel.add(senderLabel);
        infoPanel.add(titleLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(dateLabel);

        add(infoPanel, BorderLayout.EAST);
    }

    public String getViewName() {
        return viewName;
    }

    public void setEmailDetails(String sender, String title, Object score, String date) {
        senderLabel.setText("Sender: " + sender);
        titleLabel.setText("Title: " + title);
        scoreLabel.setText("Suspicion score: " + score);
        dateLabel.setText("Date: " + date);
    }

    public void setEmailText(String text) {
        emailArea.setText(text);
        emailArea.setCaretPosition(0);
    }

    // Listener hook for AppBuilder or controller
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
}
