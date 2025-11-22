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
            String kw = inputData.getKeyword().toLowerCase();
            result.removeIf(email ->
                    !(email.getTitle().toLowerCase().contains(kw) ||
                            email.getBody().toLowerCase().contains(kw)));
        }

        // filtering by sender
        if (inputData.getSender() != null && !inputData.getSender().isBlank()) {
            String snd = inputData.getSender().toLowerCase();
            result.removeIf(email ->
                    !email.getSender().toLowerCase().contains(snd));
        }

        // sorting
        if (inputData.getSortBy() != null) {
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
        return result;
    }
}
