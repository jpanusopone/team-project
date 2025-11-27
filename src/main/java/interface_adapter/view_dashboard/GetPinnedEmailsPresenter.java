package interface_adapter.view_dashboard;

import interface_adapter.ViewManagerModel;
import use_case.get_pinned_emails.GetPinnedEmailsOutputBoundary;
import use_case.get_pinned_emails.GetPinnedEmailsOutputData;

/**
 * The Presenter for the View Dashboard Use Case.
 */
public class GetPinnedEmailsPresenter implements GetPinnedEmailsOutputBoundary {

    private final DashboardViewModel dashboardViewModel;
    private final ViewManagerModel viewManagerModel;

    public GetPinnedEmailsPresenter(ViewManagerModel viewManagerModel,
                                    DashboardViewModel dashboardViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(GetPinnedEmailsOutputData outputData) {
        dashboardViewModel.getState();
    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
