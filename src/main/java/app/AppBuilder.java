package app;

import data_access.FirebaseITVerificationDataAccessObject;
import interface_adapter.it_dashboard.ItUpdateStatusPresenter;
import use_case.it_dashboard_status.ItUpdateStatusInputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusInteractor;
import use_case.it_dashboard_status.ItUpdateStatusOutputBoundary;

import data_access.FilterDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import use_case.filter.FilterInteractor;

import javax.swing.*;
import java.awt.*;

import view.LoginView;
import view.StartView;
import view.DashboardView;
import view.ViewManager;
import view.SubmitEmailView;
import view.ItDashboardView;
import view.EmailDecisionView;
import view.DashboardSelectView;
import interface_adapter.login.LoginController;
import interface_adapter.it_dashboard.ItDashboardController;
import interface_adapter.filter.ItFilterController;
import interface_adapter.login.LoginPresenter;
import use_case.login.LoginInteractor;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private DashboardView dashboardView;
    private DashboardSelectView dashboardSelectView;
    private StartView startView;
    private ItDashboardView itDashboardView;
    private EmailDecisionView emailDecisionView;

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

        // --- Setup Filter Use Case ---
        // Create the filter view model
        FilteredViewModel filteredViewModel = new FilteredViewModel();

        // Create the filter presenter
        FilterPresenter filterPresenter = new FilterPresenter(viewManagerModel, filteredViewModel);

        // Create the filter data access object
        FilterDataAccessObject filterDataAccessObject = new FilterDataAccessObject();

        // Create the filter interactor
        FilterInteractor filterInteractor = new FilterInteractor(filterDataAccessObject, filterPresenter);

        // Create the filter controller with all required dependencies (now includes dashboardSelectView)
        FilterController filterController = new FilterController(filterInteractor);

        // Connect the filtered view model to the dashboard view
        dashboardView.setFilteredViewModel(filteredViewModel);
        dashboardView.setFilterController(filterController);
        dashboardView.onViewDisplayed();

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
        // --- Build the IT update-status use case ---
        FirebaseITVerificationDataAccessObject itDao =
                new FirebaseITVerificationDataAccessObject();

        ItUpdateStatusOutputBoundary presenter =
                new ItUpdateStatusPresenter(viewManagerModel, itDashboardView);

        ItUpdateStatusInputBoundary interactor =
                new ItUpdateStatusInteractor(itDao, presenter);

        // --- Create the IT dashboard controller (uses interactor) ---
        ItDashboardController itDashboardController = new ItDashboardController(
                itDashboardView,
                emailDecisionView,
                viewManagerModel,
                interactor       // 👈 use case interactor injected here
        );

        // --- Create filter controller for IT dashboard (loads table, sets currentEmails) ---
        new ItFilterController(itDashboardView, itDashboardController);

        return this;
    }

    public AppBuilder addLoginController() {
        // Make sure addLoginView() is called first
        // 1. Create presenter
        LoginPresenter loginPresenter = new LoginPresenter(viewManagerModel);

        // 2. Create interactor (use case) with presenter
        LoginInteractor loginInteractor = new LoginInteractor(loginPresenter);

        // 3. Create controller with view, viewManagerModel, and interactor
        new LoginController(loginView, viewManagerModel, loginInteractor);

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

                });
            });
        });

        // When user presses Dashboard
        startView.addDashboardListener(e -> {
            // Load pinned emails when switching to dashboard
            dashboardView = new DashboardView();
            dashboardView.onViewDisplayed();
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
            // Load pinned emails when going back to dashboard
            dashboardView = new DashboardView();
            dashboardView.onViewDisplayed();
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