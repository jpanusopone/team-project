package entity;

import java.util.List;

public class PhishingIndicators {
    private final List<String> urls;
    private final String sender;
    private final String replyTo;
    private final Boolean displayNameMismatch;
    private final Boolean urgentLanguage;
    private final Boolean requestsSensitiveInfo;
    private final List<String> attachments;

    public PhishingIndicators(List<String> urls, String sender, String replyTo,
                              Boolean displayNameMismatch, Boolean urgentLanguage,
                              Boolean requestsSensitiveInfo, List<String> attachments) {
        this.urls = urls;
        this.sender = sender;
        this.replyTo = replyTo;
        this.displayNameMismatch = displayNameMismatch;
        this.urgentLanguage = urgentLanguage;
        this.requestsSensitiveInfo = requestsSensitiveInfo;
        this.attachments = attachments;
    }

    // Getters
    public List<String> getUrls() { return urls; }
    public String getSender() { return sender; }
    public String getReplyTo() { return replyTo; }
    public Boolean getDisplayNameMismatch() { return displayNameMismatch; }
    public Boolean getUrgentLanguage() { return urgentLanguage; }
    public Boolean getRequestsSensitiveInfo() { return requestsSensitiveInfo; }
    public List<String> getAttachments() { return attachments; }
}