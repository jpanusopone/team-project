package data_access;

import entity.Email;
import use_case.filter.FilterInputData;
import use_case.filter.FilterUserDataAccessInterface;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilterDataAccessObject implements FilterUserDataAccessInterface {

    private final List<Email> allPinnedEmails;

    public FilterDataAccessObject(List<Email> allPinnedEmails) {
        this.allPinnedEmails = allPinnedEmails;
    }

    @Override
    public List<Email> filter(FilterInputData inputData) {

        List<Email> result = new ArrayList<Email>(allPinnedEmails);

        // filtering by keyword in title
        if (inputData.getKeyword() != null && !inputData.getKeyword().isBlank()) {
            filterByKeyword(inputData, result);
        }

        // filtering by sender
        if (inputData.getSender() != null && !inputData.getSender().isBlank()) {
            filterBySender(inputData, result);
        }

        // filtering by min suspicion score
        if (inputData.getMinScore() != null) {
            filterByMinScore(inputData, result);
        }

        // filtering by max suspicion score
        if (inputData.getMaxScore() != null) {
            filterByMaxScore(inputData, result);
        }

        // sorting
        if (inputData.getSortBy() != null) {
            sort(inputData, result);
        }

        return result;
    }

    private static void filterByMaxScore(FilterInputData inputData, List<Email> result) {
        Double maxScore = inputData.getMaxScore();
        result.removeIf(email -> (email.getSuspicionScore() > maxScore) );
    }

    private static void filterByMinScore(FilterInputData inputData, List<Email> result) {
        Double minScore = inputData.getMinScore();
        result.removeIf(email -> (email.getSuspicionScore() < minScore) );
    }

    private static void sort(FilterInputData inputData, List<Email> result) {
        switch (inputData.getSortBy()) {
            case DATE_RECEIVED:
                result.sort(Comparator.comparing(Email::getDateReceived));
                break;
            case TITLE:
                result.sort(Comparator.comparing(Email::getTitle));
                break;
            case SENDER:
                result.sort(Comparator.comparing(Email::getSender));
                break;
            case SUSPICION_SCORE:
                result.sort(Comparator.comparing(Email::getSuspicionScore));
                break;
        }
    }

    private static void filterBySender(FilterInputData inputData, List<Email> result) {
        String snd = inputData.getSender().toLowerCase();
        result.removeIf(email ->
                !email.getSender().toLowerCase().contains(snd));
    }

    private static void filterByKeyword(FilterInputData inputData, List<Email> result) {
        String kw = inputData.getKeyword().toLowerCase();
        result.removeIf(email ->
                !(email.getTitle().toLowerCase().contains(kw) ||
                        email.getBody().toLowerCase().contains(kw)));
    }
}
