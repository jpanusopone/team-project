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
        List<Email> filteredEmails = outputData.getEmails();
        filteredViewModel.getState().setEmails(filteredEmails);
        filteredViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        filteredViewModel.getState().setError(error);
        filteredViewModel.firePropertyChange();
    }
}
