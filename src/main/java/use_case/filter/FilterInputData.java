package use_case.filter;

/**
 * the Input Data for the Filter Use Case.
 */
public class FilterInputData {
    private final String keyword;
    private final String sender;
    private final SortBy sortBy;

    public FilterInputData(String keyword,
                           String sender,
                           SortBy sortBy) {
        this.keyword = keyword;
        this.sender = sender;
        this.sortBy = sortBy;
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

}

