package use_case.filter;

import java.util.List;

/**
 * Output Data for the Filter Use Case.
 */
public class FilterOutputData {
    private List<String> titles;
    private List<String> senders;
    private List<String> datesReceived;
    private List<String> suspicionScores;
    private List<String> verifiedStatuses;

    public FilterOutputData(List<String> titles, List<String> senders,
                            List<String> datesReceived, List<String> suspicionScores,
                            List<String> verifiedStatuses) {
        this.titles = titles;
        this.senders = senders;
        this.datesReceived = datesReceived;
        this.suspicionScores = suspicionScores;
        this.verifiedStatuses = verifiedStatuses;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getSenders() {
        return senders;
    }

    public void setSenders(List<String> senders) {
        this.senders = senders;
    }

    public List<String> getDatesReceived() {
        return datesReceived;
    }

    public void setDatesReceived(List<String> datesReceived) {
        this.datesReceived = datesReceived;
    }

    public List<String> getSuspicionScores() {
        return suspicionScores;
    }

    public void setSuspicionScores(List<String> suspicionScores) {
        this.suspicionScores = suspicionScores;
    }

    public List<String> getVerifiedStatuses() {
        return verifiedStatuses;
    }

    public void setVerifiedStatuses(List<String> verifiedStatuses) {
        this.verifiedStatuses = verifiedStatuses;
    }
}
