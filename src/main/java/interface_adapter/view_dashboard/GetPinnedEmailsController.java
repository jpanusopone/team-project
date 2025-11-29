package interface_adapter.view_dashboard;

import use_case.get_pinned_emails.GetPinnedEmailsInputBoundary;
import use_case.get_pinned_emails.GetPinnedEmailsInputData;

/**
 * Controller for loading pinned emails into the dashboard.
 */
public class GetPinnedEmailsController {
    private final GetPinnedEmailsInputBoundary interactor;

    public GetPinnedEmailsController(GetPinnedEmailsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Execute the get pinned emails use case.
     */
    public void execute() {
        // Get all pinned emails without filters (null for all parameters)
        GetPinnedEmailsInputData inputData = new GetPinnedEmailsInputData(null, null, null, null);
        interactor.execute(inputData);
    }
}
