package interface_adapter.it_dashboard;

import javax.swing.JOptionPane;

import interface_adapter.ViewManagerModel;
import use_case.it_dashboard_status.ItUpdateStatusOutputBoundary;
import use_case.it_dashboard_status.ItUpdateStatusOutputData;
import view.ItDashboardView;

/**
 * Presenter for updating the status of an email on the IT dashboard.
 */
public class ItUpdateStatusPresenter implements ItUpdateStatusOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final ItDashboardView itDashboardView;

    /**
     * Constructs an {@code ItUpdateStatusPresenter}.
     *
     * @param viewManagerModel the view manager model
     * @param itDashboardView  the IT dashboard view
     */
    public ItUpdateStatusPresenter(ViewManagerModel viewManagerModel,
                                   ItDashboardView itDashboardView) {
        this.viewManagerModel = viewManagerModel;
        this.itDashboardView = itDashboardView;
    }

    @Override
    public void prepareSuccessView(ItUpdateStatusOutputData data) {
        JOptionPane.showMessageDialog(
                itDashboardView,
                "Email status updated to: " + data.getStatus(),
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

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
