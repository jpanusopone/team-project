package use_case.view_dashboard;

import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.FilterOutputBoundary;
import use_case.filter.FilterUserDataAccessInterface;

/**
 * The Filter Interactor
 */
public class ViewDashboardInteractor implements ViewDashboardInputBoundary {
    private final ViewDashboardUserDataAccessInterface userDataAccessObject;
    private final ViewDashboardOutputBoundary viewDashboardPresenter;

    public ViewDashboardInteractor(ViewDashboardUserDataAccessInterface userDataAccessInterface,
                            ViewDashboardOutputBoundary viewDashboardOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.viewDashboardPresenter = viewDashboardOutputBoundary;
    }
    @Override
    public void execute(ViewDashboardInputData viewDashboardInputData) {
        //TODO implement execute
    }
}
