package app;

import data_access.FilterDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.filter.ItFilterController;
import interface_adapter.it_dashboard.ItDashboardController;
import interface_adapter.login.LoginController;
import use_case.filter.FilterInteractor;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private LoginView loginView;
    private DashboardView dashboardView;
    private DashboardSelectView dashboardSelectView;
    private StartView startView;
    private ItDashboardView itDashboardView;
    private EmailDecisionView emailDecisionView;

    public AppBuilder() {
        CardLayout cardLayout = new CardLayout();
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

    public AppBuilder addDashboardSelectView() {
        dashboardSelectView = new DashboardSelectView();
        cardPanel.add(dashboardSelectView, dashboardSelectView.getViewName());

        // Add back listener to return to dashboard
        dashboardSelectView.addBackListener(e -> {
            viewManagerModel.setState(dashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        return this;
    }

    public AppBuilder addDashboardControllers() {
        // make sure addDashBoardView() and addDashboardSelectView() are called before this

        // filter
        FilterDataAccessObject filterDAO = new FilterDataAccessObject();
        FilteredViewModel filteredViewModel = new FilteredViewModel();

        // Create the filter presenter
        FilterPresenter filterPresenter = new FilterPresenter(viewManagerModel, filteredViewModel);

        FilterInteractor filterInteractor = new FilterInteractor(filterDAO, filterPresenter);
        FilterController filterController = new FilterController(filterInteractor);
        dashboardView.setFilterController(filterController);
        dashboardView.setFilteredViewModel(filteredViewModel);

        filterController.execute("", "", "0.0", "100.0", "Title");

        return this;
    }

    public AppBuilder addItDashboardView() {
        itDashboardView = new ItDashboardView();
        cardPanel.add(itDashboardView, itDashboardView.getViewName());
        return this;
    }

    public AppBuilder addEmailDecisionView() {
        emailDecisionView = new EmailDecisionView();
        cardPanel.add(emailDecisionView, emailDecisionView.getViewName());
        return this;
    }

    public AppBuilder addItDashboardControllers() {
        // Make sure addItDashboardView() and addEmailDecisionView() are called first
        ItDashboardController itDashboardController = new ItDashboardController(
                itDashboardView, emailDecisionView, viewManagerModel);
        new ItFilterController(itDashboardView, itDashboardController);
        return this;
    }

    public AppBuilder addLoginController() {
        // Make sure addLoginView() is called first
        new LoginController(loginView, viewManagerModel);
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

                // Add back to dashboard listener
                submitView.addBackToDashboardListener(backEvent -> {
                    submitView.dispose();
                    viewManagerModel.setState(dashboardView.getViewName());
                    viewManagerModel.firePropertyChange();
                });
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

        // Add back to start listener for dashboard
        dashboardView.addBackToStartListener(e -> {
            viewManagerModel.setState(startView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // Add back to dashboard listener for login
        loginView.addBackToDashboardListener(e -> {
            viewManagerModel.setState(dashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // Add back to start listener for IT dashboard
        itDashboardView.getBackButton().addActionListener(e -> {
            viewManagerModel.setState(startView.getViewName());
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
