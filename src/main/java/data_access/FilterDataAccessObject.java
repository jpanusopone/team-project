package data_access;

import entity.Email;
import use_case.filter.FilterInputData;
import use_case.filter.FilterUserDataAccessInterface;
import use_case.filter.SortBy;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Filter data access implementation using Firebase.
 * Provides filtering and sorting functionality for pinned emails on the dashboard.
 */
public class FilterDataAccessObject implements FilterUserDataAccessInterface {
    private final FirebaseEmailDataAccessObject emailDAO;

    public FilterDataAccessObject() {
        this.emailDAO = new FirebaseEmailDataAccessObject();
    }

    @Override
    public List<Email> filter(FilterInputData inputData) {
        try {
            // Get pinned emails only
            List<Email> pinnedEmails = emailDAO.getPinnedEmails();

            // Apply filters
            List<Email> filteredEmails = pinnedEmails.stream()
                    .filter(email -> matchesFilters(email,
                            inputData.getKeyword(),
                            inputData.getMinScore(),
                            inputData.getMaxScore(),
                            inputData.getSender()))
                    .collect(Collectors.toList());

            // Apply sorting
            return sortEmails(filteredEmails, inputData.getSortBy());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to filter emails from Firebase", e);
        }
    }

    /**
     * Get all pinned emails without filtering.
     *
     * @return list of all pinned emails
     */
    public List<Email> getPinnedEmails() {
        try {
            return emailDAO.getPinnedEmails();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get pinned emails from Firebase", e);
        }
    }

    /**
     * Check if an email matches the filter criteria.
     */
    private boolean matchesFilters(Email email, String keyword, Double minScore,
                                   Double maxScore, String senderDomain) {
        // Keyword filter
        if (keyword != null && !keyword.isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            boolean matchesKeyword =
                (email.getTitle() != null && email.getTitle().toLowerCase().contains(lowerKeyword)) ||
                (email.getSender() != null && email.getSender().toLowerCase().contains(lowerKeyword)) ||
                (email.getBody() != null && email.getBody().toLowerCase().contains(lowerKeyword));
            if (!matchesKeyword) return false;
        }

        // Score filters
        if (minScore != null && email.getSuspicionScore() != null && email.getSuspicionScore() < minScore) {
            return false;
        }
        if (maxScore != null && email.getSuspicionScore() != null && email.getSuspicionScore() > maxScore) {
            return false;
        }

        // Sender domain filter
        if (senderDomain != null && !senderDomain.isEmpty()) {
            if (email.getSender() == null || !email.getSender().toLowerCase().contains(senderDomain.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sort emails based on the specified criteria.
     */
    private List<Email> sortEmails(List<Email> emails, SortBy sortBy) {
        if (sortBy == null) {
            return emails;
        }

        Comparator<Email> comparator;

        switch (sortBy) {
            case TITLE:
                comparator = Comparator.comparing(Email::getTitle,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case SENDER:
                comparator = Comparator.comparing(Email::getSender,
                        Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case DATE_RECEIVED:
                comparator = Comparator.comparing(Email::getDateReceived,
                        Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            case SUSPICION_SCORE:
                comparator = Comparator.comparing(Email::getSuspicionScore,
                        Comparator.nullsLast(Comparator.reverseOrder()));
                break;
            default:
                return emails;
        }

        return emails.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}
