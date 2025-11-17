package use_case.get_pinned_emails;

/**
 * The Get Pinned Emails Interactor
 */
public class GetPinnedEmailsInteractor implements GetPinnedEmailsInputBoundary {
    private final GetPinnedEmailsUserDataAccessInterface userDataAccessObject;
    private final GetPinnedEmailsOutputBoundary getPinnedEmailsPresenter;

    public GetPinnedEmailsInteractor(GetPinnedEmailsUserDataAccessInterface userDataAccessInterface,
                                     GetPinnedEmailsOutputBoundary pinnedEmailsOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.getPinnedEmailsPresenter = pinnedEmailsOutputBoundary;
    }
    @Override
    public void execute(GetPinnedEmailsInputData viewDashboardInputData) {
        //TODO implement execute


    }
}
