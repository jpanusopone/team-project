package interface_adapter.filter;

import entity.Email;
import use_case.filter.SortBy;
import java.util.List;

/**
 * The State information representing the current filters.
 */
public class FilteredState {
    private List<Email> emails;
    private String error;
    private SortBy sortBy;
    private Double minScore;
    private Double maxScore;

    public FilteredState(FilteredState copy) {
        emails = copy.emails;
        sortBy = copy.sortBy;
        minScore = copy.minScore;
        maxScore = copy.maxScore;
        error = copy.error;
    }

    public FilteredState() {

    }

    // --- GETTERS ---

    public List<Email> getEmails() {
        return emails;
    }

    public String getError() {
        return error;
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

    // --- SETTERS ---

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void setMinScore(Double minScore) {
        this.minScore = minScore;
    }

    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }
}
