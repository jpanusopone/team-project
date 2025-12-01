// use_case/it_dashboard_status/ItUpdateStatusInputBoundary.java

package use_case.it_dashboard_status;

/**
 * Input boundary for updating the status of an email in the IT dashboard.
 * Defines the request model required by the use case interactor.
 */
public interface ItUpdateStatusInputBoundary {

    /**
     * Executes the update status use case.
     *
     * @param inputData the data containing document ID and new status
     */
    void updateStatus(ItUpdateStatusInputData inputData);
}
