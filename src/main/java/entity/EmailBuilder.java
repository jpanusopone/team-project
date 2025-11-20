package entity;

import java.time.LocalDateTime;
import java.util.List;

public class EmailBuilder {
    private final Email email;

    public EmailBuilder() {
        this.email = new Email();
    }

    public EmailBuilder id(int id) {
        email.setId(id);
        return this;
    }

    public EmailBuilder title(String title) {
        email.setTitle(title);
        return this;
    }

    public EmailBuilder sender(String sender) {
        email.setSender(sender);
        return this;
    }

    public EmailBuilder body(String body) {
        email.setBody(body);
        return this;
    }

    public EmailBuilder pinned(Boolean pinned) {
        email.setPinned(pinned);
        return this;
    }

    public EmailBuilder pinnedDate(LocalDateTime pinnedDate) {
        email.setPinnedDate(pinnedDate);
        return this;
    }

    public EmailBuilder dateReceived(LocalDateTime dateReceived) {
        email.setDateReceived(dateReceived);
        return this;
    }

    public EmailBuilder suspicionScore(Double score) {
        email.setSuspicionScore(score);
        return this;
    }

    public EmailBuilder explanation(String explanation) {
        email.setExplanation(explanation);
        return this;
    }

    public EmailBuilder links(List<String> links) {
        email.setLinks(links);
        return this;
    }

    public EmailBuilder verifiedStatus(String verifiedStatus) {
        email.setVerifiedStatus(verifiedStatus);
        return this;
    }

    public Email build() {
        return email;
    }
}
