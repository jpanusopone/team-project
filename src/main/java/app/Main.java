package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel for better UI appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore and use default look and feel
        }

        // Launch application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            AppBuilder app = new AppBuilder();
            app.show();
        });
    }
}
