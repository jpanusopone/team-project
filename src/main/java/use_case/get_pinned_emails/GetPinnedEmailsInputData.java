package use_case.get_pinned_emails;

import java.time.LocalDateTime;

/**
 * Input Data for the Get Pinned Emails use case.
 */
public class GetPinnedEmailsInputData {
    private final String sender;
    private final String title;
    private final Double suspicionScore;
    private final LocalDateTime date;

    public GetPinnedEmailsInputData(String sender,
                                    String title,
                                    Double suspicionScore,
                                    LocalDateTime date) {

        this.sender = sender;
        this.title = title;
        this.suspicionScore = suspicionScore;
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public String getTitle() {
        return title;
    }

    public Double getSuspicionScore() {
        return suspicionScore;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
