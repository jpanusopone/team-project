package app;

import javax.swing.*;

import interface_adapter.filter.FilterController;
import view.DashboardView;

public class Main {
    public static void main(String[] args) {
//        DashboardView view = new DashboardView();
//        new FilterController(view);
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                //add submit view later
                .addLoginView()
                .addDashBoardView()
                .addStartView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

