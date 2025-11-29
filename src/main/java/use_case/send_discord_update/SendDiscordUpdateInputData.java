package use_case.send_discord_update;

import entity.Email;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Input Data for sending updates in Discord server.
 */
public class SendDiscordUpdateInputData {
    private final int id;
    private final String title;
    private final String sender;
    private final String body;
    private final LocalDateTime dateReceived;

    private final double suspicionScore;
    private final String explanation;
    private final List<String> links;

    private final LocalDateTime pinnedDate;
    private final String verifiedStatus;

    public SendDiscordUpdateInputData(Email email) {
        this.id = email.getId();
        this.title = email.getTitle();
        this.sender = email.getSender();
        this.body = email.getBody();
        this.dateReceived = email.getDateReceived();
        this.suspicionScore = email.getSuspicionScore();
        this.explanation = email.getExplanation();
        this.links = email.getLinks();
        this.pinnedDate = email.getPinnedDate();
        this.verifiedStatus = email.getVerifiedStatus();
    }

    // Getters

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

    public LocalDateTime getPinnedDate() {
        return pinnedDate;
    }

    public String getVerifiedStatus() {
        return verifiedStatus;
    }
}
