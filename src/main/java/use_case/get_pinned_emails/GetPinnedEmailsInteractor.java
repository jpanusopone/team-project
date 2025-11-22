package use_case.get_pinned_emails;

import entity.Email;

import java.util.List;

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
        try {
            // Get pinned emails from database
            var pinnedEmails = userDataAccessObject.getPinnedEmails(viewDashboardInputData);

            // Create output data
            GetPinnedEmailsOutputData outputData = new GetPinnedEmailsOutputData(pinnedEmails);

            // Send to presenter
            getPinnedEmailsPresenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            // Handle any errors
            getPinnedEmailsPresenter.prepareFailView("Failed to load pinned emails: " + e.getMessage());
        }
    }
}