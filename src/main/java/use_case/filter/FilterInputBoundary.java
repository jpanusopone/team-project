package use_case.filter;

/**
 * Input boundary for actions which are related to filtering.
 */
public interface FilterInputBoundary {

    /**
     * Executes the filter use case.
     * @param filterInputData the input data
     */
    void execute(FilterInputData filterInputData);
}
