package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import config.ApplicationConfig;
import data_access.FilterDataAccessObject;
import data_access.FirebaseITVerificationDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.filter.ItFilterController;
import interface_adapter.it_dashboard.ItDashboardController;
import interface_adapter.it_dashboard.ItUpdateStatusPresenter;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import use_case.filter.FilterInteractor;
import use_case.it_dashboard_status.ItUpdateStatusInputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusInteractor;
import use_case.it_dashboard_status.ItUpdateStatusOutputBoundary;
import use_case.login.LoginInteractor;
import view.DashboardSelectView;
import view.DashboardView;
import view.EmailDecisionView;
import view.ItDashboardView;
import view.LoginView;
import view.StartView;
import view.SubmitEmailView;
import view.ViewManager;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private DashboardView dashboardView;
    private DashboardSelectView dashboardSelectView;
    private StartView startView;
    private ItDashboardView itDashboardView;
    private EmailDecisionView emailDecisionView;

    /**
     * Constructs an AppBuilder and initializes the card layout.
     */
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Adds the login view to the card layout.
     *
     * @return this builder for chaining
     */
    public AppBuilder addLoginView() {
        loginView = new LoginView();
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Adds the main dashboard view to the card layout.
     *
     * @return this builder for chaining
     */
    public AppBuilder addDashBoardView() {
        dashboardView = new DashboardView();
        cardPanel.add(dashboardView, dashboardView.getViewName());
        return this;
    }

    /**
     * Adds the dashboard selection view and wires its back button
     * to return to the main dashboard.
     *
     * @return this builder for chaining
     */
    public AppBuilder addDashboardSelectView() {
        dashboardSelectView = new DashboardSelectView();
        cardPanel.add(dashboardSelectView, dashboardSelectView.getViewName());

        // Add back listener to return to dashboard
        dashboardSelectView.addBackListener(event -> {
            viewManagerModel.setState(dashboardView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        return this;
    }

    /**
     * Wires the filter use case to the dashboard view.
     *
     * @return this builder for chaining
     */
    public AppBuilder addDashboardControllers() {
        // make sure addDashBoardView() and addDashboardSelectView() are called before this

        final FilteredViewModel filteredViewModel = new FilteredViewModel();
        final FilterPresenter filterPresenter = new FilterPresenter(viewManagerModel, filteredViewModel);
        final FilterDataAccessObject filterDataAccessObject = new FilterDataAccessObject();
        final FilterInteractor filterInteractor = new FilterInteractor(filterDataAccessObject, filterPresenter);
        final FilterController filterController = new FilterController(filterInteractor);

        // Connect the filtered view model to the dashboard view
        dashboardView.setFilteredViewModel(filteredViewModel);
        dashboardView.setFilterController(filterController);
        dashboardView.onViewDisplayed();

        return this;
    }

    /**
     * Adds the IT dashboard view to the card layout.
     *
     * @return this builder for chaining
     */
    public AppBuilder addItDashboardView() {
        itDashboardView = new ItDashboardView();
        cardPanel.add(itDashboardView, itDashboardView.getViewName());
        return this;
    }

    /**
     * Adds the email decision view to the card layout.
     *
     * @return this builder for chaining
     */
    public AppBuilder addEmailDecisionView() {
        emailDecisionView = new EmailDecisionView();
        cardPanel.add(emailDecisionView, emailDecisionView.getViewName());
        return this;
    }

    /**
     * Wires the IT dashboard to the update-status use case and
     * filter controller.
     *
     * @return this builder for chaining
     */
    public AppBuilder addItDashboardControllers() {
        // 1) DAO for IT verification
        final FirebaseITVerificationDataAccessObject itDao =
                new FirebaseITVerificationDataAccessObject();

        // 2) Presenter
        final ItUpdateStatusOutputBoundary presenter =
                new ItUpdateStatusPresenter(viewManagerModel, itDashboardView);

        // 3) Interactor
        final ItUpdateStatusInputBoundary interactor =
                new ItUpdateStatusInteractor(itDao, presenter);

        // 4) IT dashboard controller (handles table + decision view)
        final ItDashboardController itDashboardController =
                new ItDashboardController(
                        itDashboardView,
                        emailDecisionView,
                        viewManagerModel,
                        interactor
                );
        // 5) IT filter controller (loads table, sets currentEmails)
        new ItFilterController(itDashboardView, itDashboardController);
        return this;
    }



    /**
     * Wires the login view to its presenter and use case interactor.
     *
     * @return this builder for chaining
     */
    public AppBuilder addLoginController() {
        // Make sure addLoginView() is called first
        // 1. Create presenter
        final LoginPresenter loginPresenter = new LoginPresenter(viewManagerModel);

        // 2. Create interactor (use case) with presenter
        final LoginInteractor loginInteractor = new LoginInteractor(loginPresenter);

        // 3. Create controller with view, viewManagerModel, and interactor
        new LoginController(loginView, viewManagerModel, loginInteractor);

        return this;
    }

    /**
     * Adds the start view and connects navigation buttons to the
     * appropriate views.
     *
     * @return this builder for chaining
     */
    public AppBuilder addStartView() {
        startView = new StartView();
        cardPanel.add(startView, startView.getViewName());

        // When user presses Submit Phishing Email
        startView.addSubmitPhishingListener(event -> handleSubmitPhishing());

        // When user presses Dashboard
        startView.addDashboardListener(event -> handleDashboardPressed());

        // When user presses IT login
        startView.addItLoginListener(event -> {
            viewManagerModel.setState(loginView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // Add back to start listener for dashboard
        dashboardView.addBackToStartListener(event -> {
            viewManagerModel.setState(startView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        // Add back to dashboard listener for login
        loginView.addBackToDashboardListener(event -> {
            // Load pinned emails when going back to dashboard
            handleDashboardPressed();
        });

        // Add back to start listener for IT dashboard
        itDashboardView.getBackButton().addActionListener(event -> {
            viewManagerModel.setState(startView.getViewName());
            viewManagerModel.firePropertyChange();
        });

        return this;
    }

    /**
     * Opens the submit email view in a separate window.
     */
    private void handleSubmitPhishing() {
        SwingUtilities.invokeLater(() -> {
            final SubmitEmailView submitView = ApplicationConfig.createSubmitEmailView();
            submitView.setLocationRelativeTo(null);
            submitView.setVisible(true);
            submitView.addBackToDashboardListener(backEvent -> submitView.dispose());
        });
    }

    /**
     * Shows the dashboard view and refreshes its pinned emails.
     */
    private void handleDashboardPressed() {
        dashboardView = new DashboardView();
        dashboardView.onViewDisplayed();
        viewManagerModel.setState(dashboardView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    /**
     * Builds and returns the main application frame.
     *
     * @return the configured JFrame for the application
     */
    public JFrame build() {
        final JFrame application = new JFrame("PhishDetect");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(startView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
