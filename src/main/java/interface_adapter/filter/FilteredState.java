package interface_adapter.filter;

import java.util.List;

/**
 * The state information representing the current filters applied to the email list.
 */
public class FilteredState {

    private List<String> titles;
    private List<String> senders;
    private List<String> datesReceived;
    private List<String> suspicionScores;
    private List<String> verifiedStatuses;
    private String error;

    /**
     * Construct a state object containing full filter lists.
     *
     * @param titles           list of titles
     * @param senders          list of senders
     * @param datesReceived    list of received dates
     * @param suspicionScores  list of suspicion scores
     * @param verifiedStatuses list of verification statuses
     */
    public FilteredState(List<String> titles, List<String> senders,
                         List<String> datesReceived, List<String> suspicionScores,
                         List<String> verifiedStatuses) {
        this.titles = titles;
        this.senders = senders;
        this.datesReceived = datesReceived;
        this.suspicionScores = suspicionScores;
        this.verifiedStatuses = verifiedStatuses;
        this.error = null;
    }

    /**
     * Default constructor.
     */
    public FilteredState() {
        // Default constructor
    }

    // --- GETTERS ---

    public List<String> getTitles() {
        return titles;
    }

    public List<String> getSenders() {
        return senders;
    }

    public List<String> getDatesReceived() {
        return datesReceived;
    }

    public List<String> getSuspicionScores() {
        return suspicionScores;
    }

    public List<String> getVerifiedStatuses() {
        return verifiedStatuses;
    }

    public String getError() {
        return error;
    }

    // --- SETTERS ---

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public void setSenders(List<String> senders) {
        this.senders = senders;
    }

    public void setDatesReceived(List<String> datesReceived) {
        this.datesReceived = datesReceived;
    }

    public void setSuspicionScores(List<String> suspicionScores) {
        this.suspicionScores = suspicionScores;
    }

    public void setVerifiedStatuses(List<String> verifiedStatuses) {
        this.verifiedStatuses = verifiedStatuses;
    }

    public void setError(String error) {
        this.error = error;
    }
}
