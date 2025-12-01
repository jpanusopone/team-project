package data_access;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import config.FirebaseConfig;
import entity.Email;
import entity.EmailBuilder;
import use_case.save_email.SaveEmailDataAccessInterface;

/**
 * Firebase implementation for email data access.
 * Stores and retrieves emails from Firestore database.
 */
public class FirebaseEmailDataAccessObject implements SaveEmailDataAccessInterface {
    private static final String COLLECTION_EMAILS = "emails";
    private static final String FIELD_PINNED = "pinned";
    private static final String FIELD_PINNED_DATE = "pinnedDate";
    private static final String FIELD_VERIFIED_STATUS = "verifiedStatus";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_SENDER = "sender";

    private final Firestore db;

    public FirebaseEmailDataAccessObject() {
        this.db = FirebaseConfig.getFirestore();
    }

    /**
     * Save an email to Firebase.
     *
     * @param email the email to save
     * @return the saved email with generated ID
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public Email saveEmail(Email email) throws ExecutionException, InterruptedException {
        final Map<String, Object> emailData = emailToMap(email);

        final ApiFuture<DocumentReference> future =
                db.collection(COLLECTION_EMAILS).add(emailData);
        final DocumentReference docRef = future.get();

        // Update the email with the generated ID
        final EmailBuilder builder = new EmailBuilder();
        builder.id(docRef.getId().hashCode())
                .title(email.getTitle())
                .sender(email.getSender())
                .body(email.getBody())
                .pinned(email.isPinned())
                .pinnedDate(email.getPinnedDate())
                .dateReceived(email.getDateReceived())
                .suspicionScore(email.getSuspicionScore())
                .explanation(email.getExplanation())
                .links(email.getLinks())
                .verifiedStatus(email.getVerifiedStatus());

        return builder.build();
    }

    /**
     * Get an email by ID.
     *
     * @param emailId the email ID
     * @return the email if found, null otherwise
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public Email getEmailById(String emailId) throws ExecutionException, InterruptedException {
        final DocumentSnapshot document =
                db.collection(COLLECTION_EMAILS).document(emailId).get().get();

        if (!document.exists()) {
            return null;
        }

        return documentToEmail(document);
    }

    /**
     * Get all emails.
     *
     * @return list of all emails
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public List<Email> getAllEmails() throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_EMAILS).get();
        final List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream()
                .map(this::documentToEmail)
                .collect(Collectors.toList());
    }

    /**
     * Get all pinned emails.
     *
     * @return list of pinned emails
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public List<Email> getPinnedEmails() throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_EMAILS)
                .whereEqualTo(FIELD_PINNED, true)
                .orderBy(FIELD_PINNED_DATE, Query.Direction.DESCENDING)
                .get();

        final List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream()
                .map(this::documentToEmail)
                .collect(Collectors.toList());
    }

    /**
     * Update an email's pinned status.
     *
     * @param emailId the email ID
     * @param pinned whether the email is pinned
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public void updatePinnedStatus(String emailId, boolean pinned)
            throws ExecutionException, InterruptedException {
        final Map<String, Object> updates = new HashMap<>();
        updates.put(FIELD_PINNED, pinned);
        if (pinned) {
            updates.put(FIELD_PINNED_DATE, com.google.cloud.Timestamp.now());
        }

        db.collection(COLLECTION_EMAILS).document(emailId).update(updates).get();
    }

    /**
     * Update an email's verification status.
     *
     * @param emailId the email ID
     * @param status the verification status ("Pending", "Confirmed", "Safe")
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public void updateVerificationStatus(String emailId, String status)
            throws ExecutionException, InterruptedException {
        final Map<String, Object> updates = new HashMap<>();
        updates.put(FIELD_VERIFIED_STATUS, status);

        db.collection(COLLECTION_EMAILS).document(emailId).update(updates).get();
    }

    /**
     * Filter emails by criteria.
     *
     * @param keyword keyword to search in title, sender, or body
     * @param minScore minimum suspicion score
     * @param maxScore maximum suspicion score
     * @param senderDomain sender domain to filter by
     * @return list of filtered emails
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public List<Email> filterEmails(String keyword, Double minScore,
                                    Double maxScore, String senderDomain)
            throws ExecutionException, InterruptedException {
        // Firebase doesn't support complex OR queries, so we filter in memory
        final List<Email> allEmails = getAllEmails();

        return allEmails.stream()
                .filter(email -> {
                    // Keyword filter
                    if (keyword != null && !keyword.isEmpty()) {
                        final String lowerKeyword = keyword.toLowerCase();
                        final boolean matchesKeyword =
                                email.getTitle() != null
                                        && email.getTitle().toLowerCase().contains(lowerKeyword)
                                        || email.getSender() != null
                                        && email.getSender().toLowerCase().contains(lowerKeyword)
                                        || email.getBody() != null
                                        && email.getBody().toLowerCase().contains(lowerKeyword);
                        if (!matchesKeyword) {
                            return false;
                        }
                    }

                    // Score filter
                    if (minScore != null && email.getSuspicionScore() < minScore) {
                        return false;
                    }
                    if (maxScore != null && email.getSuspicionScore() > maxScore) {
                        return false;
                    }

                    // Sender domain filter
                    if (senderDomain != null && !senderDomain.isEmpty()) {
                        final String sender = email.getSender();
                        if (sender == null
                                || !sender.toLowerCase()
                                .contains(senderDomain.toLowerCase())) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Check if an email already exists in the database.
     *
     * @param title email title
     * @param sender email sender
     * @param dateReceived email date received
     * @return true if email exists, false otherwise
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public boolean emailExists(String title, String sender, LocalDateTime dateReceived)
            throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_EMAILS)
                .whereEqualTo(FIELD_TITLE, title)
                .whereEqualTo(FIELD_SENDER, sender)
                .get();

        return !future.get().getDocuments().isEmpty();
    }

    /**
     * Check if an email with the same content already exists in the database.
     * Uses content hash for efficient duplicate detection.
     *
     * @param body email body content
     * @return true if email with same content exists, false otherwise
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public boolean emailExistsByContent(String body)
            throws ExecutionException, InterruptedException {
        if (body == null || body.trim().isEmpty()) {
            return false;
        }

        final String contentHash = generateContentHash(body);

        final ApiFuture<QuerySnapshot> future = db.collection(COLLECTION_EMAILS)
                .whereEqualTo("contentHash", contentHash)
                .get();

        return !future.get().getDocuments().isEmpty();
    }

    /**
     * Generate a hash of the email content for duplicate detection.
     * Normalizes whitespace and case before hashing.
     *
     * @param content email body content
     * @return SHA-256 hash of normalized content, or a simple hash fallback
     */
    private String generateContentHash(String content) {
        if (content == null) {
            return "";
        }

        // Normalize: trim, lowercase, collapse multiple spaces
        final String normalized = content.trim()
                .toLowerCase()
                .replaceAll("\\s+", " ");

        try {
            final java.security.MessageDigest digest =
                    java.security.MessageDigest.getInstance("SHA-256");
            final byte[] hashBytes =
                    digest.digest(normalized.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Convert to hex string
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                final String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (java.security.NoSuchAlgorithmException ex) {
            // Fallback to simple hash if SHA-256 is not available
            return String.valueOf(normalized.hashCode());
        }
    }

    /**
     * Delete an email by ID.
     *
     * @param emailId the email ID to delete
     * @throws ExecutionException if the database operation fails
     * @throws InterruptedException if the operation is interrupted
     */
    public void deleteEmail(String emailId) throws ExecutionException, InterruptedException {
        db.collection(COLLECTION_EMAILS).document(emailId).delete().get();
    }

    /**
     * Convert Email entity to Firestore map.
     *
     * @param email the source Email entity
     * @return a map representation for Firestore storage
     */
    private Map<String, Object> emailToMap(Email email) {
        final Map<String, Object> map = new HashMap<>();
        if (email.getTitle() != null) {
            map.put(FIELD_TITLE, email.getTitle());
        }
        if (email.getSender() != null) {
            map.put(FIELD_SENDER, email.getSender());
        }
        if (email.getBody() != null) {
            map.put("body", email.getBody());
            // Add content hash for duplicate detection
            map.put("contentHash", generateContentHash(email.getBody()));
        }
        map.put(FIELD_PINNED, email.isPinned());

        if (email.getPinnedDate() != null) {
            map.put(FIELD_PINNED_DATE, toTimestamp(email.getPinnedDate()));
        }
        if (email.getDateReceived() != null) {
            map.put("dateReceived", toTimestamp(email.getDateReceived()));
        }

        map.put("suspicionScore", email.getSuspicionScore());
        if (email.getExplanation() != null) {
            map.put("explanation", email.getExplanation());
        }
        if (email.getLinks() != null) {
            map.put("links", email.getLinks());
        }
        if (email.getVerifiedStatus() != null) {
            map.put(FIELD_VERIFIED_STATUS, email.getVerifiedStatus());
        }

        return map;
    }

    /**
     * Convert Firestore document to Email entity.
     *
     * @param document Firestore document snapshot
     * @return an Email entity built from the document
     */
    private Email documentToEmail(DocumentSnapshot document) {
        final EmailBuilder builder = new EmailBuilder();

        builder.id(document.getId().hashCode())
                .documentId(document.getId())
                .title(document.getString(FIELD_TITLE))
                .sender(document.getString(FIELD_SENDER))
                .body(document.getString("body"));

        final Boolean pinnedValue = document.getBoolean(FIELD_PINNED);
        builder.pinned(pinnedValue != null ? pinnedValue : false);

        final com.google.cloud.Timestamp pinnedTimestamp =
                document.getTimestamp(FIELD_PINNED_DATE);
        if (pinnedTimestamp != null) {
            builder.pinnedDate(toLocalDateTime(pinnedTimestamp));
        }

        final com.google.cloud.Timestamp receivedTimestamp =
                document.getTimestamp("dateReceived");
        if (receivedTimestamp != null) {
            builder.dateReceived(toLocalDateTime(receivedTimestamp));
        }

        final Double scoreValue = document.getDouble("suspicionScore");
        builder.suspicionScore(scoreValue != null ? scoreValue : 0.0);

        builder.explanation(document.getString("explanation"));

        @SuppressWarnings("unchecked")
        final List<String> links = (List<String>) document.get("links");
        if (links != null) {
            builder.links(links);
        }

        builder.verifiedStatus(document.getString(FIELD_VERIFIED_STATUS));

        return builder.build();
    }

    /**
     * Convert LocalDateTime to Firestore Timestamp.
     *
     * @param dateTime the LocalDateTime to convert
     * @return a Firestore Timestamp representing the same instant
     */
    private com.google.cloud.Timestamp toTimestamp(LocalDateTime dateTime) {
        final Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
        return com.google.cloud.Timestamp.ofTimeSecondsAndNanos(
                instant.getEpochSecond(), instant.getNano());
    }

    /**
     * Convert Firestore Timestamp to LocalDateTime.
     *
     * @param timestamp the Firestore Timestamp to convert
     * @return a LocalDateTime representing the same instant
     */
    private LocalDateTime toLocalDateTime(com.google.cloud.Timestamp timestamp) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
                ZoneId.systemDefault()
        );
    }
}
