package app;

import javax.swing.JFrame;

public class Main {
    /**
     * Entry point for the Phish Detect application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
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
