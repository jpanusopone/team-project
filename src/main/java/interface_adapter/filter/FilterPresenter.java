package interface_adapter.filter;

import interface_adapter.ViewManagerModel;
import use_case.filter.FilterOutputBoundary;
import use_case.filter.FilterOutputData;

import java.util.ArrayList;

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
        FilteredState state = new FilteredState(
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
        FilteredState state = new FilteredState(
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
}