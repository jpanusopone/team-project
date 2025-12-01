package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents an email entity used within the application.
 */
public class Email {

    private int id;
    private String documentId;
    private String title;
    private String sender;
    private String body;
    private LocalDateTime dateReceived;
    private double suspicionScore;
    private String explanation;
    private List<String> links;
    private boolean pinned;
    private LocalDateTime pinnedDate;
    private String verifiedStatus;

    /**
     * Default constructor.
     */
    public Email() {
    }

    /**
     * Constructor using essential fields. Additional fields should be set using setters.
     *
     * @param id          email ID
     * @param title       email title
     * @param sender      sender address
     * @param body        email body
     * @param dateReceived date the email was received
     */
    public Email(int id,
                 String title,
                 String sender,
                 String body,
                 LocalDateTime dateReceived) {

        this.id = id;
        this.title = title;
        this.sender = sender;
        this.body = body;
        this.dateReceived = dateReceived;
    }

    public int getId() {
        return id;
    }

    public String getDocumentId() {
        return documentId;
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

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public double getSuspicionScore() {
        return suspicionScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public List<String> getLinks() {
        return links;
    }

    public boolean isPinned() {
        return pinned;
    }

    public LocalDateTime getPinnedDate() {
        return pinnedDate;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public void setSuspicionScore(double suspicionScore) {
        this.suspicionScore = suspicionScore;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setPinnedDate(LocalDateTime pinnedDate) {
        this.pinnedDate = pinnedDate;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }
}
