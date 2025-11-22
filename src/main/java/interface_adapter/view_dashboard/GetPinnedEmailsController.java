package interface_adapter.view_dashboard;

import use_case.get_pinned_emails.GetPinnedEmailsInputBoundary;

public class GetPinnedEmailsController {
    private final GetPinnedEmailsInputBoundary getPinnedEmailsUseCaseInteractor;

    public GetPinnedEmailsController(GetPinnedEmailsInputBoundary getPinnedEmailsUseCaseInteractor) {
        this.getPinnedEmailsUseCaseInteractor = getPinnedEmailsUseCaseInteractor;
    }

    public void execute() {
        getPinnedEmailsUseCaseInteractor.execute();
    }
}
