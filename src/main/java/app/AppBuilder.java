package app;

//import data_access.EmailDataAccessObject;
import data_access.FilterDataAccessObject;
import data_access.GetPinnedEmailsDataAccessObject;
import entity.Email;
import entity.EmailBuilder;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;
import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.view_dashboard.DashboardViewModel;
import interface_adapter.view_dashboard.GetPinnedEmailsController;
import interface_adapter.view_dashboard.GetPinnedEmailsPresenter;
import use_case.filter.FilterInteractor;
import use_case.get_pinned_emails.GetPinnedEmailsInteractor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import interface_adapter.view_dashboard.DashboardViewModel;
import use_case.filter.FilterInteractor;
//import use_case.get_pinned_emails.GetPinnedEmailsInteractor;
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

//    private GetPinnedEmailsController pinnedEmailsController;
    private FilterController filterController;

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
        new FilterController(filterInteractor);

        // Connect the filtered view model to the dashboard view
        dashboardView.setFilteredViewModel(filteredViewModel);

        // --- Setup Get Pinned Emails Use Case ---
        // Create the dashboard view model
        DashboardViewModel dashboardViewModel = new DashboardViewModel();

        // Create the get pinned emails presenter
        GetPinnedEmailsPresenter getPinnedEmailsPresenter = new GetPinnedEmailsPresenter(viewManagerModel, dashboardViewModel);

        // Create the get pinned emails data access object
        GetPinnedEmailsDataAccessObject getPinnedEmailsDataAccessObject = new GetPinnedEmailsDataAccessObject();

        // Connect the dashboard view model and controller to the dashboard view
        dashboardView.setDashboardViewModel(dashboardViewModel);

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
                    // Load pinned emails when going back to dashboard
                    dashboardView.loadPinnedEmails();
                    viewManagerModel.setState(dashboardView.getViewName());
                    viewManagerModel.firePropertyChange();
                });
            });
        });

        // When user presses Dashboard
        startView.addDashboardListener(e -> {
            // Load pinned emails when switching to dashboard
            dashboardView.loadPinnedEmails();

            viewManagerModel.setState(dashboardView.getViewName());
            viewManagerModel.firePropertyChange();

//            pinnedEmailsController.execute();

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
            dashboardView.loadPinnedEmails();
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

    // temporary method to create list of emails (DAO not available yet)
    private List<Email> createSampleEmails() {
        List<Email> emails = new ArrayList<>();

        Email email1 = new EmailBuilder()
                .id(1)
                .title("Your PayPal Account is Suspended")
                .sender("support@paypal.com")
                .body("")
                .pinned(true)
                .pinnedDate(LocalDateTime.now())
                .suspicionScore(0.92)
                .dateReceived(LocalDateTime.now().minusDays(1))
                .explanation("blah blah blah")
                .links(new ArrayList<String>())
                .verifiedStatus("verified")
                .build();

        Email email2 = new EmailBuilder()
                .id(2)
                .title("Urgent: Update Your Bank Information")
                .sender("security@bankofamerica.com")
                .body("Please click the link below to update your account information immediately.")
                .pinned(false)
                .pinnedDate(null)
                .suspicionScore(0.85)
                .dateReceived(LocalDateTime.now().minusDays(2))
                .explanation("The email asks for sensitive info via a suspicious link.")
                .links(List.of("http://fakebank.com/update"))
                .verifiedStatus("unverified")
                .build();

        Email email3 = new EmailBuilder()
                .id(3)
                .title("You've Won a Free iPhone!")
                .sender("promotions@fakestore.com")
                .body("Click here to claim your free iPhone now.")
                .pinned(true)
                .pinnedDate(LocalDateTime.now().minusHours(5))
                .suspicionScore(0.95)
                .dateReceived(LocalDateTime.now().minusDays(1))
                .explanation("Too good to be true offer; likely phishing.")
                .links(List.of("http://scamwebsite.com/claim"))
                .verifiedStatus("unverified")
                .build();

        emails.add(email1);
        emails.add(email2);
        emails.add(email3);

        return emails;
    }

}
