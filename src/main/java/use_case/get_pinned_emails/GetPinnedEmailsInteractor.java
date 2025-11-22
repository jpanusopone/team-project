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
    public void execute(GetPinnedEmailsInputData getPinnedEmailsInputData) {

        try {
            List<Email> pinnedEmails = userDataAccessObject.getPinnedEmails(getPinnedEmailsInputData);
            GetPinnedEmailsOutputData outputData = new GetPinnedEmailsOutputData(pinnedEmails);
            getPinnedEmailsPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            getPinnedEmailsPresenter.prepareFailView("Failed to get pinned emails: " + e.getMessage());
        }


    }
}
