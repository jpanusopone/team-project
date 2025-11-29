package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                //add submit view later
                .addLoginView()
                .addDashBoardView()
                .addDashboardControllers()
                .addStartView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

    }
}


