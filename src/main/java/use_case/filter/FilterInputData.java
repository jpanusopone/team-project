package use_case.filter;

/**
 * The Input Data for the Filter Use Case.
 */
public class FilterInputData {
    private final String keyword;
    private final String sender;
    private final SortBy sortBy;
    private final Double minScore;
    private final Double maxScore;

    public FilterInputData(String keyword,
                           String sender,
                           SortBy sortBy,
                           Double minScore,
                           Double maxScore) {
        this.keyword = keyword;
        this.sender = sender;
        this.sortBy = sortBy;
        this.minScore = minScore;
        this.maxScore = maxScore;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getSender() {
        return sender;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public Double getMinScore() {
        return minScore;
    }

    public Double getMaxScore() {
        return maxScore;
    }

}
