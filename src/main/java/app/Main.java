package app;

import view.SubmitEmailView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
//        DashboardView view = new DashboardView();
//        new FilterController(view);
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


        SwingUtilities.invokeLater(() -> new SubmitEmailView().setVisible(true));
    }
}
