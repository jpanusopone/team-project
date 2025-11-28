package interface_adapter.filter;

import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;

/**
 * Controller for the Filter Use Case.
 */
public class FilterController {
    private final FilterInputBoundary filterInteractor;

    /**
     * Executes the Filter Use Case
     *
     * @param filterInteractor the Filter Interactor
     */

    public FilterController(FilterInputBoundary filterInteractor) {
        this.filterInteractor = filterInteractor;
    }
    public void execute(String keyword,
                        String sender,
                        String minScoreStr,
                        String maxScoreStr,
                        String sortValue) {

        double minScore = 0.0;
        double maxScore = 100.0;

        try {
            if (!minScoreStr.isBlank()) minScore = Double.parseDouble(minScoreStr);
            if (!maxScoreStr.isBlank()) maxScore = Double.parseDouble(maxScoreStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid score format");
        }

        SortBy sortBy = switch (sortValue) {
            case "Title" -> SortBy.TITLE;
            case "Sender" -> SortBy.SENDER;
            case "Date Received" -> SortBy.DATE_RECEIVED;
            case "Suspicion Score" -> SortBy.SUSPICION_SCORE;
            default -> SortBy.TITLE;
        };

        FilterInputData data = new FilterInputData(
                keyword,
                sender,
                sortBy,
                minScore,
                maxScore
        );

        filterInteractor.execute(data);
        }
    }




