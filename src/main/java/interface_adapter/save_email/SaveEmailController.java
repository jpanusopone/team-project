package interface_adapter.save_email;

import java.time.LocalDateTime;
import java.util.List;

import use_case.save_email.SaveEmailInputBoundary;
import use_case.save_email.SaveEmailInputData;

/**
 * Controller for saving emails.
 * Converts view-layer parameters into {@link SaveEmailInputData}
 * and passes them to the save email interactor.
 */
public class SaveEmailController {

    private final SaveEmailInputBoundary interactor;

    /**
     * Constructs a SaveEmailController.
     *
     * @param interactor the input boundary for the save email use case
     */
    public SaveEmailController(SaveEmailInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Execute the save email use case with the provided data.
     *
     * @param title the email title
     * @param sender the email sender
     * @param body the email body content
     * @param dateReceived when the email was received
     * @param suspicionScore computed suspicion score
     * @param explanation explanation text for the score
     * @param links any extracted links from the email
     */
    public void execute(String title,
                        String sender,
                        String body,
                        LocalDateTime dateReceived,
                        double suspicionScore,
                        String explanation,
                        List<String> links) {

        final SaveEmailInputData inputData = new SaveEmailInputData(
                title,
                sender,
                body,
                dateReceived,
                suspicionScore,
                explanation,
                links
        );
        interactor.execute(inputData);
    }
}
