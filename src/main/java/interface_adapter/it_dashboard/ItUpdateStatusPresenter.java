package interface_adapter.it_dashboard;

import javax.swing.JOptionPane;

import interface_adapter.ViewManagerModel;
import use_case.it_dashboard_status.ItUpdateStatusOutputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusOutputData;
import view.ItDashboardView;

// ✅ imports for the dashboard filter use case:
import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.FilterConstants;
import use_case.filter.SortBy;

/**
 * Presenter for updating the status of an email on the IT dashboard.
 */
public class ItUpdateStatusPresenter implements ItUpdateStatusOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final ItDashboardView itDashboardView;
    private final FilterInputBoundary filterInteractor;   // ✅ new

    /**
     * Constructs an {@code ItUpdateStatusPresenter}.
     *
     * @param viewManagerModel the view manager model
     * @param itDashboardView  the IT dashboard view
     * @param filterInteractor the dashboard filter use case (for refreshing regular dashboard)
     */
    public ItUpdateStatusPresenter(ViewManagerModel viewManagerModel,
                                   ItDashboardView itDashboardView,
                                   FilterInputBoundary filterInteractor) {
        this.viewManagerModel = viewManagerModel;
        this.itDashboardView = itDashboardView;
        this.filterInteractor = filterInteractor;
    }

    @Override
    public void prepareSuccessView(ItUpdateStatusOutputData data) {
        JOptionPane.showMessageDialog(
                itDashboardView,
                "Email status updated to: " + data.getStatus(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        // ✅ Refresh the *regular* dashboard data via the filter use case
        if (filterInteractor != null) {
            FilterInputData reload = new FilterInputData(
                    "",                         // keyword
                    "",                         // sender
                    SortBy.TITLE,               // default sort
                    FilterConstants.MIN_SCORE,  // default min
                    FilterConstants.MAX_SCORE   // default max
            );
            filterInteractor.execute(reload);
        }

        // Go back to IT dashboard
        viewManagerModel.setState(itDashboardView.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(
                itDashboardView,
                errorMessage,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
