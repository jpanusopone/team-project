package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for {@link Email} entities.
 * Provides a fluent API and sensible default values.
 */
public class EmailBuilder {

    private final Email email;

    /**
     * Creates a new builder with default values set on the email.
     */
    public EmailBuilder() {
        this.email = new Email();
        // Set default values
        email.setDateReceived(LocalDateTime.now());
        email.setLinks(new ArrayList<>());
        email.setExplanation("");
        email.setSuspicionScore(0.0);
        email.setPinned(false);
        email.setVerifiedStatus("Pending");
    }

    /**
     * Sets the email ID.
     *
     * @param id the email ID
     * @return this builder
     */
    public EmailBuilder id(int id) {
        email.setId(id);
        return this;
    }

    /**
     * Sets the document ID (for Firebase).
     *
     * @param documentId the Firebase document ID
     * @return this builder
     */
    public EmailBuilder documentId(String documentId) {
        email.setDocumentId(documentId);
        return this;
    }

    /**
     * Sets the email title (subject).
     *
     * @param title the title
     * @return this builder
     */
    public EmailBuilder title(String title) {
        email.setTitle(title);
        return this;
    }

    /**
     * Sets the email sender.
     *
     * @param sender the sender
     * @return this builder
     */
    public EmailBuilder sender(String sender) {
        email.setSender(sender);
        return this;
    }

    /**
     * Sets the email body and auto-extracts metadata
     * such as subject, sender and links from the header.
     *
     * @param body the email body
     * @return this builder
     */
    public EmailBuilder body(String body) {
        email.setBody(body);
        autoExtractMetadata(body);
        return this;
    }

    /**
     * Sets the date the email was received.
     *
     * @param dateReceived the received date
     * @return this builder
     */
    public EmailBuilder dateReceived(LocalDateTime dateReceived) {
        email.setDateReceived(dateReceived);
        return this;
    }

    /**
     * Sets the suspicion score.
     *
     * @param score the suspicion score
     * @return this builder
     */
    public EmailBuilder suspicionScore(double score) {
        email.setSuspicionScore(score);
        return this;
    }

    /**
     * Sets the explanation text.
     *
     * @param explanation the explanation
     * @return this builder
     */
    public EmailBuilder explanation(String explanation) {
        email.setExplanation(explanation);
        return this;
    }

    /**
     * Sets the list of links contained in the email.
     *
     * @param links the list of links
     * @return this builder
     */
    public EmailBuilder links(List<String> links) {
        email.setLinks(links);
        return this;
    }

    /**
     * Sets the verification status.
     *
     * @param status the verification status
     * @return this builder
     */
    public EmailBuilder verifiedStatus(String status) {
        email.setVerifiedStatus(status);
        return this;
    }

    /**
     * Sets whether the email is pinned.
     *
     * @param pinned true if pinned
     * @return this builder
     */
    public EmailBuilder pinned(boolean pinned) {
        email.setPinned(pinned);
        return this;
    }

    /**
     * Sets the date when the email was pinned.
     *
     * @param date the pinned date
     * @return this builder
     */
    public EmailBuilder pinnedDate(LocalDateTime date) {
        email.setPinnedDate(date);
        return this;
    }

    /**
     * Builds and returns the configured {@link Email}.
     *
     * @return the built email
     */
    public Email build() {
        return email;
    }

    /**
     * Automatically extracts metadata such as subject, sender and
     * any lines that contain URLs from the email header.
     *
     * @param body the email body text
     */
    private void autoExtractMetadata(String body) {
        if (body != null) {
            final List<String> collectedLinks = email.getLinks();
            final String[] lines = body.split("\\R");

            for (String line : lines) {
                final String lower = line.toLowerCase();

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
                if (line.isBlank()) {
                    break;
                }
            }

            email.setLinks(collectedLinks);
        }
    }
}
