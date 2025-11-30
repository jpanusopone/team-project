package use_case.save_email;

import java.time.LocalDateTime;
import java.util.List;

public class SaveEmailInputData {
    private final String title;
    private final String sender;
    private final String body;
    private final LocalDateTime dateReceived;
    private final double suspicionScore;
    private final String explanation;
    private final List<String> links;

    public SaveEmailInputData(String title, String sender, String body,
                              LocalDateTime dateReceived, double suspicionScore,
                              String explanation, List<String> links) {
        this.title = title;
        this.sender = sender;
        this.body = body;
        this.dateReceived = dateReceived;
        this.suspicionScore = suspicionScore;
        this.explanation = explanation;
        this.links = links;
    }

    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public String getBody() { return body; }
    public LocalDateTime getDateReceived() { return dateReceived; }
    public double getSuspicionScore() { return suspicionScore; }
    public String getExplanation() { return explanation; }
    public List<String> getLinks() { return links; }
}
