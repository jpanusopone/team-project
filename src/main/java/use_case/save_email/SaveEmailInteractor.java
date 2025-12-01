package use_case.save_email;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import entity.Email;
import entity.EmailBuilder;

/**
 * Interactor for the Save Email use case.
 * Validates input, checks for duplicates, constructs the Email entity,
 * and delegates persistence to the data access layer.
 */
public class SaveEmailInteractor implements SaveEmailInputBoundary {

    private final SaveEmailDataAccessInterface dataAccess;
    private final SaveEmailOutputBoundary presenter;

    /**
     * Constructs a SaveEmailInteractor.
     *
     * @param dataAccess the data access object used to save emails
     * @param presenter  the presenter used to report success or failure
     */
    public SaveEmailInteractor(SaveEmailDataAccessInterface dataAccess,
                               SaveEmailOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveEmailInputData inputData) {
        SaveEmailOutputData validationError = null;

        if (inputData.getSender() == null || inputData.getSender().trim().isEmpty()) {
            validationError = new SaveEmailOutputData(false, "Sender cannot be empty");
        }
        else if (inputData.getTitle() == null || inputData.getTitle().trim().isEmpty()) {
            validationError = new SaveEmailOutputData(false, "Subject cannot be empty");
        }
        else if (inputData.getBody() == null || inputData.getBody().trim().isEmpty()) {
            validationError = new SaveEmailOutputData(false, "Email body cannot be empty");
        }

        if (validationError != null) {
            presenter.presentError(validationError);
        }
        else {
            try {
                if (dataAccess.emailExistsByContent(inputData.getBody())) {
                    final SaveEmailOutputData duplicateOutput =
                            new SaveEmailOutputData(false,
                                    "This email has already been submitted to the system.");
                    presenter.presentError(duplicateOutput);
                }
                else {
                    LocalDateTime dateReceived = inputData.getDateReceived();
                    if (dateReceived == null) {
                        dateReceived = LocalDateTime.now();
                    }

                    final EmailBuilder builder = new EmailBuilder();
                    final Email email = builder
                            .title(inputData.getTitle())
                            .sender(inputData.getSender())
                            .body(inputData.getBody())
                            .dateReceived(dateReceived)
                            .suspicionScore(inputData.getSuspicionScore())
                            .explanation(inputData.getExplanation())
                            .links(inputData.getLinks())
                            .pinned(true)
                            .pinnedDate(LocalDateTime.now())
                            .verifiedStatus("Pending")
                            .build();

                    dataAccess.saveEmail(email);

                    final SaveEmailOutputData successOutput =
                            new SaveEmailOutputData(true,
                                    "Email successfully pinned to dashboard!");
                    presenter.presentSuccess(successOutput);
                }
            }
            catch (ExecutionException ex) {
                final SaveEmailOutputData errorOutput =
                        new SaveEmailOutputData(false,
                                "Failed to pin email: " + ex.getMessage());
                presenter.presentError(errorOutput);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                final SaveEmailOutputData errorOutput =
                        new SaveEmailOutputData(false,
                                "Failed to pin email: " + ex.getMessage());
                presenter.presentError(errorOutput);
            }
        }
    }
}
