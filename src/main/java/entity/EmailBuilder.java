package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailBuilder {

    private final Email email;

    public EmailBuilder() {
        this.email = new Email();
        email.setDateReceived(LocalDateTime.now()); // default
        email.setLinks(new ArrayList<>());
        email.setExplanation("");
        email.setSuspicionScore(0.0);
        email.setPinned(false);
        email.setVerifiedStatus("Pending");
    }

    public EmailBuilder id(int id) {
        email.setId(id);
        return this;
    }

    public EmailBuilder documentId(String documentId) {
        email.setDocumentId(documentId);
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
        autoExtractMetadata(body);
        return this;
    }

    public EmailBuilder dateReceived(LocalDateTime dateReceived) {
        email.setDateReceived(dateReceived);
        return this;
    }

    public EmailBuilder suspicionScore(double score) {
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

    public EmailBuilder verifiedStatus(String status) {
        email.setVerifiedStatus(status);
        return this;
    }

    public EmailBuilder pinned(boolean pinned) {
        email.setPinned(pinned);
        return this;
    }

    public EmailBuilder pinnedDate(LocalDateTime date) {
        email.setPinnedDate(date);
        return this;
    }

    public Email build() {
        return email;
    }


    private void autoExtractMetadata(String body) {
        if (body == null) return;

        List<String> collectedLinks = email.getLinks();
        String[] lines = body.split("\\R");

        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.startsWith("subject:")) {
                email.setTitle(line.substring("subject:".length()).trim());
            }
            else if (lower.startsWith("from:")) {
                email.setSender(line.substring("from:".length()).trim());
            }

            if (line.contains("http://") || line.contains("https://")) {
                collectedLinks.add(line.trim());
            }

            // blank line marks end of header section
            if (line.isBlank()) break;
        }

        email.setLinks(collectedLinks);
    }
}