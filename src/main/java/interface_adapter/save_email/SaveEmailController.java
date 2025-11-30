package interface_adapter.save_email;

import use_case.save_email.SaveEmailInputBoundary;
import use_case.save_email.SaveEmailInputData;

import java.time.LocalDateTime;
import java.util.List;

public class SaveEmailController {
    private final SaveEmailInputBoundary interactor;

    public SaveEmailController(SaveEmailInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String title, String sender, String body,
                       LocalDateTime dateReceived, double suspicionScore,
                       String explanation, List<String> links) {
        SaveEmailInputData inputData = new SaveEmailInputData(
                title, sender, body, dateReceived, suspicionScore, explanation, links);
        interactor.execute(inputData);
    }
}
