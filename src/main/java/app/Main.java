package app;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    /**
     * Entry point for the Phish Detect application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }
        catch (UnsupportedLookAndFeelException ex) {
            // You might want to log this instead in a real app
            ex.printStackTrace();
        }

        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginView()
                .addDashBoardView()
                .addDashboardSelectView()
                .addDashboardControllers()
                .addItDashboardView()
                .addEmailDecisionView()
                .addItDashboardControllers()
                .addLoginController()
                .addStartView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

    }
}
