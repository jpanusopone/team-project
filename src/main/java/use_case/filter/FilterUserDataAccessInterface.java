package use_case.filter;

import entity.Email;
import java.util.List;

/**
 * The DAO interface for the Filter Use Case.
 */
public interface FilterUserDataAccessInterface {

    /**
     * Updates the system to filter emails based on the criteria.
     * @param inputData the filter criteria
     * @return the filtered list of emails
     */
    List<Email> filter(FilterInputData inputData);
}
