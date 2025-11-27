package app;

//import data_access.EmailDataAccessObject;
import data_access.FilterDataAccessObject;
import entity.Email;
import entity.EmailBuilder;
import interface_adapter.ViewManagerModel;
import interface_adapter.filter.FilterController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import interface_adapter.filter.FilterPresenter;
import interface_adapter.filter.FilteredViewModel;
import use_case.filter.FilterInteractor;
//import use_case.get_pinned_emails.GetPinnedEmailsInteractor;
import view.LoginView;
import view.StartView;
import view.DashboardView;
import view.ViewManager;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private LoginView loginView;
    private DashboardView dashboardView;
    private StartView startView;

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

    public AppBuilder addDashboardControllers() {
        // make sure addDashBoardView() is called before this

        // filter
        FilterDataAccessObject filterDAO = new FilterDataAccessObject(createSampleEmails());
        FilteredViewModel filteredViewModel = new FilteredViewModel();
        FilterPresenter filterPresenter = new FilterPresenter(viewManagerModel, filteredViewModel);
        FilterInteractor filterInteractor = new FilterInteractor(filterDAO, filterPresenter);
        filterController = new FilterController(filterInteractor);  // its constructor should add listeners
        dashboardView.setFilterController(filterController);
        dashboardView.setFilteredViewModel(filteredViewModel);

        // pinned emails
//        EmailDataAccessObject emailDAO = new EmailDataAccessObject(createSampleEmails());
//        DashboardViewModel dashboardViewModel = new DashboardViewModel();
//        GetPinnedEmailsPresenter pinnedEmailsPresenter = new GetPinnedEmailsPresenter(viewManagerModel, dashboardViewModel);
//        GetPinnedEmailsInteractor pinnedEmailsInteractor = new GetPinnedEmailsInteractor(emailDAO, pinnedEmailsPresenter);
//        pinnedEmailsController = new GetPinnedEmailsController(pinnedEmailsInteractor);
//        dashboardView.setPinnedEmailsController(pinnedEmailsController);
//        dashboardView.setDashboardViewModel(dashboardViewModel);

//        dashboardView.setFilteredViewModel(filteredViewModel);
//        dashboardView.setPinnedEmailsController(pinnedEmailsController);
//        dashboardView.setDashboardViewModel(dashboardViewModel);
//
//        pinnedEmailsController.execute();
        filterController.execute("", "", null, 0.0, 100.0);

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

//            pinnedEmailsController.execute();

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
