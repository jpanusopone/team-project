package use_case.save_email;

import entity.Email;
import entity.EmailBuilder;

import java.time.LocalDateTime;

public class SaveEmailInteractor implements SaveEmailInputBoundary {
    private final SaveEmailDataAccessInterface dataAccess;
    private final SaveEmailOutputBoundary presenter;

    public SaveEmailInteractor(SaveEmailDataAccessInterface dataAccess,
                               SaveEmailOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveEmailInputData inputData) {
        // Validate input
        if (inputData.getSender() == null || inputData.getSender().trim().isEmpty()) {
            presenter.presentError(new SaveEmailOutputData(false, "Sender cannot be empty"));
            return;
        }

        if (inputData.getTitle() == null || inputData.getTitle().trim().isEmpty()) {
            presenter.presentError(new SaveEmailOutputData(false, "Subject cannot be empty"));
            return;
        }

        if (inputData.getBody() == null || inputData.getBody().trim().isEmpty()) {
            presenter.presentError(new SaveEmailOutputData(false, "Email body cannot be empty"));
            return;
        }

        try {
            // Check for duplicates
            if (dataAccess.emailExistsByContent(inputData.getBody())) {
                presenter.presentError(new SaveEmailOutputData(false, 
                    "This email has already been submitted to the system."));
                return;
            }

            // Create email entity
            EmailBuilder builder = new EmailBuilder();
            Email email = builder
                    .title(inputData.getTitle())
                    .sender(inputData.getSender())
                    .body(inputData.getBody())
                    .dateReceived(inputData.getDateReceived() != null ? 
                        inputData.getDateReceived() : LocalDateTime.now())
                    .suspicionScore(inputData.getSuspicionScore())
                    .explanation(inputData.getExplanation())
                    .links(inputData.getLinks())
                    .pinned(true)
                    .pinnedDate(LocalDateTime.now())
                    .verifiedStatus("Pending")
                    .build();

            // Save email
            dataAccess.saveEmail(email);

            presenter.presentSuccess(new SaveEmailOutputData(true, 
                "Email successfully pinned to dashboard!"));

        } catch (Exception e) {
            presenter.presentError(new SaveEmailOutputData(false, 
                "Failed to pin email: " + e.getMessage()));
        }
    }
}
