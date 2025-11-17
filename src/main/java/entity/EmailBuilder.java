package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmailBuilder {

    private int id = 0;
    private String title;
    private String sender;
    private String body;
    private LocalDateTime dateReceived = LocalDateTime.now();
    private double suspicionScore = 0;
    private String explanation = "";
    private List<String> links = new ArrayList<>();

    public EmailBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public EmailBuilder setBody(String body) {
        this.body = body;
        autoExtractMetadata(body);
        return this;
    }

    public EmailBuilder setSuspicionScore(double suspicionScore) {
        this.suspicionScore = suspicionScore;
        return this;
    }

    public EmailBuilder setExplanation(String explanation) {
        this.explanation = explanation;
        return this;
    }

    public EmailBuilder setLinks(List<String> links) {
        this.links = links;
        return this;
    }

    public Email build() {
        return new Email(
                id,
                title != null ? title : "(no subject)",
                sender != null ? sender : "(unknown sender)",
                body,
                dateReceived,
                suspicionScore,
                explanation,
                links
        );
    }

    private void autoExtractMetadata(String body) {
        if (body == null) return;

        String[] lines = body.split("\\R");
        for (String line : lines) {
            String lower = line.toLowerCase();

            if (lower.startsWith("subject:")) {
                title = line.substring("subject:".length()).trim();
            } else if (lower.startsWith("from:")) {
                sender = line.substring("from:".length()).trim();
            }

            if (line.contains("http://") || line.contains("https://")) {
                links.add(line.trim());
            }

            if (line.isBlank()) {
                // blank line usually separates headers from body
                break;
            }
        }
    }
}