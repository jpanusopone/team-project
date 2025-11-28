package use_case.filter;

import java.util.List;

import entity.Email;

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
        else if (filterInputData.getMinScore() != null && filterInputData.getMaxScore() != null
                && (filterInputData.getMinScore() > filterInputData.getMaxScore())) {
            filterPresenter.prepareFailView("Minimum score cannot be greater than maximum score.");
        }
        else {
            final List<Email> filteredEmails;
            try {
                filteredEmails = filterUserDataAccessObject.filter(filterInputData);
                if (filteredEmails == null || filteredEmails.isEmpty()) {
                    filterPresenter.prepareFailView("No emails matched.");
                }
                else {
                    final FilterOutputData filterOutputData = new FilterOutputData(filteredEmails);
                    filterPresenter.prepareSuccessView(filterOutputData);
                }
            }
            catch (RuntimeException exception) {
                filterPresenter.prepareFailView("Failed to load filtered emails.");
            }

        }

    }

}
