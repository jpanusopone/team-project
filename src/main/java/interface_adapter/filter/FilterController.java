package interface_adapter.filter;

import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;

/**
 * Controller for the Filter Use Case.
 */
public class FilterController {
    private FilterInputBoundary filterInteractor;

    public FilterController(FilterInputBoundary filterInteractor) {
        this.filterInteractor =  filterInteractor;
    }

    /**
     * Executes the Filter Use Case
     * @param keyword the keyword the user wishes to filter by
     * @param sender the sender the user wishes to filter by
     * @param sortBy sorts by title, sender, date received, or suspicion score
     */
    public void execute(String keyword, String sender, SortBy sortBy) {
        FilterInputData inputData = new FilterInputData(
                keyword,
                sender,
                sortBy,
                null,
                null
        );
        filterInteractor.execute(inputData);
    }

}

