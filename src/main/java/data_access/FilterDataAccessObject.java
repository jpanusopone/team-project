package data_access;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import entity.Email;
import use_case.filter.FilterInputData;
import use_case.filter.FilterUserDataAccessInterface;
import use_case.filter.SortBy;

/**
 * Filter data access implementation using Firebase.
 * Provides filtering and sorting functionality for pinned emails on the dashboard.
 */
public class FilterDataAccessObject implements FilterUserDataAccessInterface {

    private final FirebaseEmailDataAccessObject emailDao;

    public FilterDataAccessObject() {
        this.emailDao = new FirebaseEmailDataAccessObject();
    }

    /**
     * Filter pinned emails from Firebase based on the given criteria.
     *
     * @param inputData the filter criteria
     * @return list of emails matching the filter criteria
     * @throws RuntimeException if filtering fails due to Firebase errors
     */
    @Override
    public List<Email> filter(FilterInputData inputData) {
        try {
            final List<Email> pinnedEmails = emailDao.getPinnedEmails();

            final List<Email> filteredEmails = pinnedEmails.stream()
                    .filter(email -> {
                        return matchesFilters(
                                email,
                                inputData.getKeyword(),
                                inputData.getMinScore(),
                                inputData.getMaxScore(),
                                inputData.getSender()
                        );
                    })
                    .collect(Collectors.toList());

            return sortEmails(filteredEmails, inputData.getSortBy());
        }
        catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException("Failed to filter emails from Firebase", ex);
        }
    }

    /**
     * Get all pinned emails without filtering.
     *
     * @return list of all pinned emails
     * @throws RuntimeException if pinned emails cannot be retrieved
     */
    public List<Email> getPinnedEmails() {
        try {
            return emailDao.getPinnedEmails();
        }
        catch (ExecutionException | InterruptedException ex) {
            throw new RuntimeException("Failed to get pinned emails from Firebase", ex);
        }
    }

    private boolean matchesFilters(Email email, String keyword, Double minScore,
                                   Double maxScore, String senderDomain) {

        boolean matches = true;

        if (!matchesKeyword(email, keyword)) {
            matches = false;
        }
        else if (!matchesScore(email, minScore, maxScore)) {
            matches = false;
        }
        else {
            matches = matchesSender(email, senderDomain);
        }

        return matches;
    }

    private boolean matchesKeyword(Email email, String keyword) {
        boolean matches = true;

        if (keyword != null && !keyword.isEmpty()) {
            final String lowerKeyword = keyword.toLowerCase();

            matches = email.getTitle() != null
                    && email.getTitle().toLowerCase().contains(lowerKeyword)
                    || email.getSender() != null
                    && email.getSender().toLowerCase().contains(lowerKeyword)
                    || email.getBody() != null
                    && email.getBody().toLowerCase().contains(lowerKeyword);
        }

        return matches;
    }

    private boolean matchesScore(Email email, Double minScore, Double maxScore) {
        boolean matches = true;

        if (minScore != null && email.getSuspicionScore() < minScore) {
            matches = false;
        }
        else if (maxScore != null && email.getSuspicionScore() > maxScore) {
            matches = false;
        }

        return matches;
    }

    private boolean matchesSender(Email email, String senderDomain) {
        boolean matches = true;

        if (senderDomain != null && !senderDomain.isEmpty()) {
            matches = email.getSender() != null
                    && email.getSender().toLowerCase().contains(senderDomain.toLowerCase());
        }

        return matches;
    }

    /**
     * Sort emails based on the specified criteria.
     *
     * @param emails the list of emails to sort
     * @param sortBy the sorting criterion
     * @return the sorted list of emails
     */
    private List<Email> sortEmails(List<Email> emails, SortBy sortBy) {
        List<Email> result = emails;

        if (sortBy != null) {
            final Comparator<Email> comparator;

            switch (sortBy) {
                case TITLE:
                    comparator = Comparator.comparing(
                            Email::getTitle,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );
                    result = emails.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                    break;

                case SENDER:
                    comparator = Comparator.comparing(
                            Email::getSender,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    );
                    result = emails.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                    break;

                case DATE_RECEIVED:
                    comparator = Comparator.comparing(
                            Email::getDateReceived,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    );
                    result = emails.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                    break;

                case SUSPICION_SCORE:
                    comparator = Comparator.comparing(
                            Email::getSuspicionScore,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    );
                    result = emails.stream()
                            .sorted(comparator)
                            .collect(Collectors.toList());
                    break;

                default:
                    break;
            }
        }

        return result;
    }
}
