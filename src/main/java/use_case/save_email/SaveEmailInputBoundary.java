package use_case.save_email;

/**
 * Input boundary for executing the Save Email use case.
 */
public interface SaveEmailInputBoundary {

    /**
     * Executes the save email use case using the provided input data.
     *
     * @param inputData the data required to save an email
     */
    void execute(SaveEmailInputData inputData);
}
