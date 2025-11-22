package entity;

import java.time.LocalDateTime;
import java.util.List;

public class Email {
    private int id;
    private String title;
    private String sender;
    private String body;
    private Boolean pinned;
    private LocalDateTime pinnedDate;
    private LocalDateTime dateReceived;
    private Double suspicionScore;
    private String explanation;
    private List<String> links;
    private String verifiedStatus;

    void setId(int id) { this.id = id; }
    void setTitle(String title) { this.title = title; }
    void setSender(String sender) { this.sender = sender; }
    void setBody(String body) { this.body = body; }
    void setPinned(Boolean pinned) { this.pinned = pinned; }
    void setPinnedDate(LocalDateTime pinnedDate) { this.pinnedDate = pinnedDate; }
    void setDateReceived(LocalDateTime dateReceived) { this.dateReceived = dateReceived; }
    void setSuspicionScore(Double suspicionScore) { this.suspicionScore = suspicionScore; }
    void setExplanation(String explanation) { this.explanation = explanation; }
    void setLinks(List<String> links) { this.links = links; }
    void setVerifiedStatus(String verifiedStatus) { this.verifiedStatus = verifiedStatus; }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public LocalDateTime getPinnedDate() {
        return pinnedDate;
    }

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public Double getSuspicionScore() {
        return suspicionScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<String> getLinks() {
        return links;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }


    public Email() {}

}
