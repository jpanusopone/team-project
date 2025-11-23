package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;

import javax.swing.*;
import java.awt.*;

import view.LoginView;
import view.StartView;
import view.DashboardView;
import view.ViewManager;
import view.SubmitEmailView;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private DashboardView dashboardView;
    private StartView startView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginView = new LoginView();
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addDashBoardView(){
        dashboardView = new DashboardView();
        cardPanel.add(dashboardView, dashboardView.getViewName());
        return this;
    }

    public AppBuilder addDashboardControllers() {
        // make sure addDashBoardView() is called before this
        new FilterController(dashboardView);  // its constructor should add listeners
        return this;
    }

    public AppBuilder addStartView(){
        startView = new StartView();
        cardPanel.add(startView, startView.getViewName());

        // When user presses Submit Phishing Email
        startView.addSubmitPhishingListener(e -> {
            // Open the Submit Email window as a separate JFrame
            SwingUtilities.invokeLater(() -> {
                SubmitEmailView submitView = new SubmitEmailView();
                submitView.setLocationRelativeTo(null);
                submitView.setVisible(true);
            });
        });

        // When user presses Dashboard
        startView.addDashboardListener(e -> {
            viewManagerModel.setState(dashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // When user presses IT login
        startView.addItLoginListener(e -> {
            viewManagerModel.setState(loginView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        return this;
    }
    public JFrame build(){
        final JFrame application = new JFrame("PhishDetect");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(startView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
