package data_access;

import entity.Email;
import use_case.filter.FilterInputData;
import use_case.filter.FilterUserDataAccessInterface;

import java.util.List;

public class FilterDataAccessObject implements FilterUserDataAccessInterface {

    private final List<Email> allPinnedEmails;

    public FilterDataAccessObject(List<Email> allPinnedEmails) {
        this.allPinnedEmails = allPinnedEmails;
    }

    @Override
    public List<Email> filter(FilterInputData inputData) {
        return null;
    }
}
