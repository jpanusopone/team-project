package use_case.filter;

import java.util.ArrayList;
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
                    // Extract each attribute into separate lists
                    List<String> titles = new ArrayList<>();
                    List<String> senders = new ArrayList<>();
                    List<String> datesReceived = new ArrayList<>();
                    List<String> suspicionScores = new ArrayList<>();
                    List<String> verifiedStatuses = new ArrayList<>();

                    for (Email email : filteredEmails) {
                        titles.add(email.getTitle());
                        senders.add(email.getSender());
                        datesReceived.add(email.getDateReceived().toString());
                        suspicionScores.add(String.valueOf(email.getSuspicionScore()));
                        verifiedStatuses.add(email.getVerifiedStatus());
                    }

                    final FilterOutputData filterOutputData = new FilterOutputData(
                            titles, senders, datesReceived, suspicionScores, verifiedStatuses
                    );
                    filterPresenter.prepareSuccessView(filterOutputData);
                }
            }
            catch (RuntimeException exception) {
                filterPresenter.prepareFailView("Failed to load filtered emails.");
            }

        }

    }

}