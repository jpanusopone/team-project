package app;

import javax.swing.*;

import interface_adapter.filter.FilterController;
import view.DashboardView;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addDashBoardView()
                .addItDashboardView()
                .addEmailDecisionView()
                .addDashboardControllers()
                .addItDashboardControllers()
                .addStartView()
                .addLoginController()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

