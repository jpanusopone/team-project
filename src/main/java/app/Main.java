package app;

import view.SubmitEmailView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
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
