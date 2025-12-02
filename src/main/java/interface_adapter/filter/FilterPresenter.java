package interface_adapter.filter;

import java.util.ArrayList;

import interface_adapter.ViewManagerModel;
import use_case.filter.FilterOutputBoundary;
import use_case.filter.FilterOutputData;

/**
 * Presenter for the filter use case.
 * Updates the {@link FilteredViewModel} based on the filter results.
 */
public class FilterPresenter implements FilterOutputBoundary {

    private final FilteredViewModel filteredViewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Construct a presenter for filter results.
     *
     * @param viewManagerModel   the global view manager model
     * @param filteredViewModel  the view model storing filtered state
     */
    public FilterPresenter(ViewManagerModel viewManagerModel,
                           FilteredViewModel filteredViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.filteredViewModel = filteredViewModel;
    }

    @Override
    public void prepareSuccessView(FilterOutputData outputData) {
        final FilteredState state = new FilteredState(
                outputData.getTitles(),
                outputData.getSenders(),
                outputData.getDatesReceived(),
                outputData.getSuspicionScores(),
                outputData.getVerifiedStatuses()
        );
        state.setError(null);

        filteredViewModel.setState(state);
        filteredViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final FilteredState state = new FilteredState(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        state.setError(error);

        filteredViewModel.setState(state);
        filteredViewModel.firePropertyChange();
    }

    public void updateStatusInDashboard(int row, String newStatus) {
        FilteredState state = filteredViewModel.getState();

        state.getVerifiedStatuses().set(row, newStatus);

        filteredViewModel.setState(state);
        filteredViewModel.firePropertyChange();
    }
}
