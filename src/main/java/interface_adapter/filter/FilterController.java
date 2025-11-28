package interface_adapter.filter;

import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;

/**
 * Controller for the Filter Use Case.
 */
public class FilterController {
    private final FilterInputBoundary filterInteractor;

    public FilterController(FilterInputBoundary filterInteractor) {
        this.filterInteractor = filterInteractor;
    }

    /**
     * Executes the Filter Use Case.
     *
     * @param keyword the string to search for in the titles of the emails
     * @param sender the sender to search for
     * @param minScoreStr the minimum suspicion score
     * @param maxScoreStr the maximum suspicion score
     * @param sortValue the chosen sort value
     * @throws RuntimeException if min/max score are in invalid format
     */
    public void execute(String keyword,
                        String sender,
                        String minScoreStr,
                        String maxScoreStr,
                        String sortValue) {

        final double minScore = 0.0;
        final double maxScore = 100.0;

        try {
            if (!minScoreStr.isBlank()) {
                minScore = Double.parseDouble(minScoreStr);
            }
            if (!maxScoreStr.isBlank()) {
                maxScore = Double.parseDouble(maxScoreStr);
            }
        }
        catch (NumberFormatException exception) {
            throw new RuntimeException("Invalid score format");
        }

        final SortBy sortBy = switch (sortValue) {
            case "Title" -> SortBy.TITLE;
            case "Sender" -> SortBy.SENDER;
            case "Date Received" -> SortBy.DATE_RECEIVED;
            case "Suspicion Score" -> SortBy.SUSPICION_SCORE;
            default -> SortBy.TITLE;
        };

        final FilterInputData data = new FilterInputData(
                keyword,
                sender,
                sortBy,
                minScore,
                maxScore
        );

        filterInteractor.execute(data);
        }
}

