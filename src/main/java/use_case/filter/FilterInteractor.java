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

        // Convert Email objects to string lists for presentation layer
        List<String> titles = new java.util.ArrayList<>();
        List<String> senders = new java.util.ArrayList<>();
        List<String> datesReceived = new java.util.ArrayList<>();
        List<String> suspicionScores = new java.util.ArrayList<>();
        List<String> verifiedStatuses = new java.util.ArrayList<>();

        for (Email email : filteredEmails) {
            titles.add(email.getTitle() != null ? email.getTitle() : "Untitled");
            senders.add(email.getSender() != null ? email.getSender() : "Unknown");
            datesReceived.add(email.getDateReceived() != null ?
                    email.getDateReceived().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                    "N/A");
            suspicionScores.add(String.format("%.1f", email.getSuspicionScore()));
            verifiedStatuses.add(email.getVerifiedStatus() != null ? email.getVerifiedStatus() : "Pending");
        }

        FilterOutputData filterOutputData = new FilterOutputData(
                titles, senders, datesReceived, suspicionScores, verifiedStatuses
        );
        filterPresenter.prepareSuccessView(filterOutputData);
    }

}
