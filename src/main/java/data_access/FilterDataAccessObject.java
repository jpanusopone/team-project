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
        System.out.println("=== FilterDAO.filter() called ===");
        System.out.println("Keyword: '" + inputData.getKeyword() + "'");
        System.out.println("Sender: '" + inputData.getSender() + "'");
        System.out.println("Starting with " + allPinnedEmails.size() + " emails");

        List<Email> result = new ArrayList<Email>(allPinnedEmails);

        // filtering by keyword in title
        if (inputData.getKeyword() != null && !inputData.getKeyword().isBlank()) {
            String kw = inputData.getKeyword().toLowerCase();
            System.out.println("Filtering by keyword: " + kw);
            result.removeIf(email ->
                    !(email.getTitle().toLowerCase().contains(kw) ||
                            email.getBody().toLowerCase().contains(kw)));
            System.out.println("After keyword filter: " + result.size() + " emails");

        }

        // filtering by sender
        if (inputData.getSender() != null && !inputData.getSender().isBlank()) {
            String snd = inputData.getSender().toLowerCase();
            System.out.println("Filtering by sender: " + snd);
            result.removeIf(email ->
                    !email.getSender().toLowerCase().contains(snd));
            System.out.println("After sender filter: " + result.size() + " emails");
        }

        // sorting
        if (inputData.getSortBy() != null) {
            System.out.println("Sorting by: " + inputData.getSortBy());
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

        // show pinned only
//        result.removeIf(email -> !email.getPinned());
        System.out.println("Final result: " + result.size() + " emails");
        return result;
    }
}
