// use_case/it_dashboard_status/ItUpdateStatusOutputBoundary.java

package use_case.it_dashboard_status;

/**
 * Output boundary for the IT dashboard update-status use case.
 * Implementations are responsible for preparing the view
 * after an email's verification status has been updated.
 */
public interface ItUpdateStatusOutputBoundary {

    /**
     * Prepare the success view for a successful status update.
     *
     * @param data the output data containing the updated status
     */
    void prepareSuccessView(ItUpdateStatusOutputData data);

    /**
     * Prepare the failure view when the status update fails.
     *
     * @param errorMessage the error message to display to the user
     */
    void prepareFailView(String errorMessage);
}
