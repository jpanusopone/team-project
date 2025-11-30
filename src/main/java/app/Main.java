package app;

import use_case.send_discord_update.SendDiscordUpdateInteractor;
import view.SubmitEmailView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addDashBoardView()
                .addDashboardControllers()
                .addStartView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

//        SendDiscordUpdateInteractor sdui = new SendDiscordUpdateInteractor();
//        sdui.excute("test");
    }
}
