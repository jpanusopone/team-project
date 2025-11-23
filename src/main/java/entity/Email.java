package entity;

import java.time.LocalDateTime;
import java.util.List;

public class Email {

    private int id;
    private String title;
    private String sender;
    private String body;
    private LocalDateTime dateReceived;

    private double suspicionScore;
    private String explanation;
    private List<String> links;

    private boolean pinned;
    private LocalDateTime pinnedDate;
    private String verifiedStatus; // e.g., "Pending", "Confirmed", "Safe"

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public String getBody() { return body; }
    public Boolean getPinned() { return pinned; }
    public LocalDateTime getPinnedDate() { return pinnedDate; }
    public LocalDateTime getDateReceived() { return dateReceived; }
    public Double getSuspicionScore() { return suspicionScore; }
    public String getExplanation() { return explanation; }
    public List<String> getLinks() { return links; }
    public String getVerifiedStatus() { return verifiedStatus; }

    public Email() {}

    public Email(int id,
                 String title,
                 String sender,
                 String body,
                 LocalDateTime dateReceived,
                 double suspicionScore,
                 String explanation,
                 List<String> links,
                 boolean pinned,
                 LocalDateTime pinnedDate,
                 String verifiedStatus) {

        this.id = id;
        this.title = title;
        this.sender = sender;
        this.body = body;
        this.dateReceived = dateReceived;
        this.suspicionScore = suspicionScore;
        this.explanation = explanation;
        this.links = links;
        this.pinned = pinned;
        this.pinnedDate = pinnedDate;
        this.verifiedStatus = verifiedStatus;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSender() { return sender; }
    public String getBody() { return body; }
    public LocalDateTime getDateReceived() { return dateReceived; }
    public double getSuspicionScore() { return suspicionScore; }
    public String getExplanation() { return explanation; }
    public List<String> getLinks() { return links; }
    public boolean isPinned() { return pinned; }
    public LocalDateTime getPinnedDate() { return pinnedDate; }
    public String getVerifiedStatus() { return verifiedStatus; }

    // Setters (if other use cases need to update these)
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setSender(String sender) { this.sender = sender; }
    public void setBody(String body) { this.body = body; }
    public void setDateReceived(LocalDateTime dateReceived) { this.dateReceived = dateReceived; }
    public void setSuspicionScore(double suspicionScore) { this.suspicionScore = suspicionScore; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public void setLinks(List<String> links) { this.links = links; }
    public void setPinned(boolean pinned) { this.pinned = pinned; }
    public void setPinnedDate(LocalDateTime pinnedDate) { this.pinnedDate = pinnedDate; }
    public void setVerifiedStatus(String verifiedStatus) { this.verifiedStatus = verifiedStatus; }
}