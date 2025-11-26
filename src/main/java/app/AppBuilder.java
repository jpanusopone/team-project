package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.itFilterController;
import interface_adapter.login.loginController;
import interface_adapter.it_dashboard.ItDashboardController;

import javax.swing.*;
import java.awt.*;

import view.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private DashboardView dashboardView;
    private ItDashboardView itDashboardView;
    private EmailDecisionView emailDecisionView;
    private StartView startView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView() {
        loginView = new LoginView();
        cardPanel.add(loginView, loginView.getViewName());

//        // When user presses cancel
//        loginView.addCancelListener(e -> {
//            viewManagerModel.setState(startView.getViewName());  // whatever the viewName is
//            viewManagerModel.firePropertyChange();
//        });

//        // When user presses login
//        loginView.addLoginListener(e -> {
//            viewManagerModel.setState(itDashboardView.getViewName());  // whatever the viewName is
//            viewManagerModel.firePropertyChange();
//        });
        return this;
    }

    public AppBuilder addItDashboardView() {
        itDashboardView = new ItDashboardView();
        cardPanel.add(itDashboardView, itDashboardView.getViewName());
        return this;
    }

    public AppBuilder addItDashboardControllers() {
        new itFilterController(itDashboardView);
        new ItDashboardController(itDashboardView, emailDecisionView, viewManagerModel);
        return this;
    }

    public AppBuilder addEmailDecisionView() {
        emailDecisionView = new EmailDecisionView();
        cardPanel.add(emailDecisionView, emailDecisionView.getViewName());
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

    public AppBuilder addLoginController() {
        // Make sure addLoginView() and addItDashboardView() and addStartView() were called first
        new loginController(loginView, viewManagerModel);
        return this;
    }

    public AppBuilder addStartView(){
        startView = new StartView();
        cardPanel.add(startView, startView.getViewName());

        // When user presses Submit Phishing Email
        startView.addSubmitPhishingListener(e -> {
            viewManagerModel.setState("submit-phish");  // whatever the viewName is
            viewManagerModel.firePropertyChange();
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
