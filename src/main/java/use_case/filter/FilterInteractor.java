package use_case.filter;

import entity.Email;
import java.util.List;

/**
 * The Filter Interactor.
 */
public class FilterInteractor implements FilterInputBoundary {
    private final FilterUserDataAccessInterface filterUserDataAccessObject;
    private final FilterOutputBoundary filterPresenter;

    public FilterInteractor(FilterUserDataAccessInterface filterDataAccessInterface,
                            FilterOutputBoundary filterOutputBoundary) {
        this.filterUserDataAccessObject = filterDataAccessInterface;
        this.filterPresenter = filterOutputBoundary;
    }
    @Override
    public void execute(FilterInputData filterInputData) {
        if (filterInputData == null) {
            filterPresenter.prepareFailView("Filter cannot be null.");
        }

        if (filterInputData.getMinScore() != null && filterInputData.getMaxScore() != null) {
            if (filterInputData.getMinScore() > filterInputData.getMaxScore()) {
                filterPresenter.prepareFailView("Minimum score cannot be greater than maximum score.");
                return;
            }
        }

        List<Email> filteredEmails;
        try {
            filteredEmails = filterUserDataAccessObject.filter(filterInputData);
        } catch (Exception E) {
            filterPresenter.prepareFailView("Failed to load filtered emails.");
            return;
        }

        if (filteredEmails == null || filteredEmails.isEmpty()) {
            filterPresenter.prepareFailView("No emails matched.");
            return;
        }

        FilterOutputData filterOutputData = new FilterOutputData(filteredEmails);
        filterPresenter.prepareSuccessView(filterOutputData);
    }

}
