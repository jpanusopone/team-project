package entity;

import java.util.List;

/**
 * Represents detailed phishing indicators extracted from an email.
 * Includes URL analysis, sender inconsistencies, tone, and attachment risks.
 */
public class PhishingIndicators {

    private final List<String> urls;
    private final String sender;
    private final String replyTo;
    private final Boolean displayNameMismatch;
    private final Boolean urgentLanguage;
    private final Boolean requestsSensitiveInfo;
    private final List<String> attachments;

    /**
     * Constructs a set of phishing indicators.
     *
     * @param urls list of extracted URLs
     * @param sender the email sender field
     * @param replyTo the reply-to address
     * @param displayNameMismatch whether display name differs from sender
     * @param urgentLanguage whether the email uses urgency cues
     * @param requestsSensitiveInfo whether sensitive info is requested
     * @param attachments list of attachments in the email
     */
    public PhishingIndicators(
            List<String> urls,
            String sender,
            String replyTo,
            Boolean displayNameMismatch,
            Boolean urgentLanguage,
            Boolean requestsSensitiveInfo,
            List<String> attachments) {

        this.urls = urls;
        this.sender = sender;
        this.replyTo = replyTo;
        this.displayNameMismatch = displayNameMismatch;
        this.urgentLanguage = urgentLanguage;
        this.requestsSensitiveInfo = requestsSensitiveInfo;
        this.attachments = attachments;
    }

    /**
     * Returns all URLs found in the email.
     *
     * @return list of URLs
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * Returns the sender address.
     *
     * @return sender string
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the reply-to address.
     *
     * @return reply-to value
     */
    public String getReplyTo() {
        return replyTo;
    }

    /**
     * Indicates whether the display name mismatches the sender field.
     *
     * @return true if mismatch exists
     */
    public Boolean getDisplayNameMismatch() {
        return displayNameMismatch;
    }

    /**
     * Indicates whether the email uses urgent or pressuring language.
     *
     * @return true if urgency is detected
     */
    public Boolean getUrgentLanguage() {
        return urgentLanguage;
    }

    /**
     * Indicates whether the email requests sensitive or private information.
     *
     * @return true if such requests appear
     */
    public Boolean getRequestsSensitiveInfo() {
        return requestsSensitiveInfo;
    }

    /**
     * Returns a list of attachments included in the email.
     *
     * @return list of attachments
     */
    public List<String> getAttachments() {
        return attachments;
    }
}
