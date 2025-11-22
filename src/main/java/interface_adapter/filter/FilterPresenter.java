package interface_adapter.filter;

import entity.Email;
import interface_adapter.ViewManagerModel;
import use_case.filter.FilterOutputBoundary;
import use_case.filter.FilterOutputData;

import java.util.List;

public class FilterPresenter implements FilterOutputBoundary {

    private final FilteredViewModel filteredViewModel;
    private final ViewManagerModel viewManagerModel;

    public FilterPresenter(ViewManagerModel viewManagerModel,
                           FilteredViewModel filteredViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.filteredViewModel = filteredViewModel;
    }

    @Override
    public void prepareSuccessView(FilterOutputData outputData) {
        FilteredState state = filteredViewModel.getState();
        state.setEmails(outputData.getEmails());
        state.setError(null);

        filteredViewModel.setState(state);
        filteredViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        FilteredState state = filteredViewModel.getState();
        state.setError(error);

        filteredViewModel.setState(state);
        filteredViewModel.firePropertyChange();
    }
}
