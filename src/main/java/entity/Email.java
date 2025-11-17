package entity;

import java.time.LocalDateTime;
import java.util.List;

public class Email {

    private final int id;
    private final String title;
    private final String sender;
    private final String body;
    private final LocalDateTime dateReceived;
    private final double suspicionScore;
    private final String explanation;
    private final List<String> links;

    public Email(int id,
                 String title,
                 String sender,
                 String body,
                 LocalDateTime dateReceived,
                 double suspicionScore,
                 String explanation,
                 List<String> links) {

        this.id = id;
        this.title = title;
        this.sender = sender;
        this.body = body;
        this.dateReceived = dateReceived;
        this.suspicionScore = suspicionScore;
        this.explanation = explanation;
        this.links = links;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public String getBody() { return body; }
    public LocalDateTime getDateReceived() { return dateReceived; }
    public double getSuspicionScore() { return suspicionScore; }
    public String getExplanation() { return explanation; }
    public List<String> getLinks() { return links; }
}