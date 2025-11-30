package interface_adapter.filter;

import use_case.filter.FilterConstants;
import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;

/**
 * Controller for the Filter User Case.
 */

public class FilterController {
    private final FilterInputBoundary filterInteractor;

    public FilterController(FilterInputBoundary filterInteractor) {
        this.filterInteractor = filterInteractor;
    }

    /**
     * Executes the Filter Use Case.
     *
     * @param keyword the string to search for in the titles of the emails
     * @param sender the sender to search for
     * @param minScoreStr the minimum suspicion score
     * @param maxScoreStr the maximum suspicion score
     * @param sortValue the chosen sort value
     * @throws RuntimeException if min/max score are in invalid format
     */
    public void execute(String keyword,
                        String sender,
                        String minScoreStr,
                        String maxScoreStr,
                        String sortValue) {

        double minScore = FilterConstants.MIN_SCORE;
        double maxScore = FilterConstants.MAX_SCORE;

        try {
            if (!minScoreStr.isBlank()) {
                minScore = Double.parseDouble(minScoreStr);
            }
            if (!maxScoreStr.isBlank()) {
                maxScore = Double.parseDouble(maxScoreStr);
            }
        }
        catch (NumberFormatException exception) {
            throw new RuntimeException("Invalid score format");
        }

        final SortBy sortBy = switch (sortValue) {
            case "Sender" -> SortBy.SENDER;
            case "Date Received" -> SortBy.DATE_RECEIVED;
            case "Suspicion Score" -> SortBy.SUSPICION_SCORE;
            default -> SortBy.TITLE;
        };

        final FilterInputData data = new FilterInputData(
                keyword,
                sender,
                sortBy,
                minScore,
                maxScore
        );

        filterInteractor.execute(data);
    }
}


///**
// * Convert dropdown selection to SortBy enum
// */
//private SortBy convertToSortBy(String selection) {
//    switch (selection) {
//        case "Sender":
//            return SortBy.SENDER;
//        case "Date":
//            return SortBy.DATE_RECEIVED;
//        case "Suspicion Score":
//            return SortBy.SUSPICION_SCORE;
//        default:
//            return SortBy.DATE_RECEIVED;
//    }
//}
//
///**
// * Open email details view when user clicks on a table row
// */
//private void openEmailDetails(int row) {
//    // Get the actual Email object from the view
//    entity.Email email = view.getEmailAtRow(row);
//
//    if (email == null) {
//        JOptionPane.showMessageDialog(view,
//                "Could not load email details.",
//                "Error",
//                JOptionPane.ERROR_MESSAGE);
//        return;
//    }
//
//    // Format date
//    String dateStr = email.getDateReceived() != null ?
//            email.getDateReceived().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
//            "N/A";
//
//    String status = email.getVerifiedStatus() != null ? email.getVerifiedStatus() : "Pending";
//
//    // Build email content (just the body)
//    String emailText;
//    if (email.getBody() != null && !email.getBody().isEmpty()) {
//        emailText = email.getBody();
//    } else {
//        emailText = "[No email body content]";
//    }
//
//    // Build analysis section (phishing analysis + link risk)
//    StringBuilder analysisText = new StringBuilder();
//
//    // Phishing Explanation
//    if (email.getExplanation() != null && !email.getExplanation().isEmpty()) {
//        analysisText.append("═══════════════════════════════════════════════════════\n");
//        analysisText.append("  PHISHING RISK ANALYSIS\n");
//        analysisText.append("═══════════════════════════════════════════════════════\n\n");
//        analysisText.append(email.getExplanation()).append("\n\n");
//    }
//
//    // Link Risk Analysis
//    if (email.getLinks() != null && !email.getLinks().isEmpty()) {
//        analysisText.append("═══════════════════════════════════════════════════════\n");
//        analysisText.append("  LINK RISK ANALYSIS\n");
//        analysisText.append("═══════════════════════════════════════════════════════\n\n");
//
//        boolean hasHighRiskLinks = false;
//        for (String link : email.getLinks()) {
//            String linkAnalysis = analyzeLinkRisk(link);
//            analysisText.append(linkAnalysis).append("\n\n");
//
//            if (isLinkSuspicious(link)) {
//                hasHighRiskLinks = true;
//            }
//        }
//
//        if (hasHighRiskLinks) {
//            analysisText.append("⚠️  WARNING: This email contains suspicious links!\n");
//            analysisText.append("    DO NOT CLICK on any links in this email.\n\n");
//        }
//    } else {
//        analysisText.append("═══════════════════════════════════════════════════════\n");
//        analysisText.append("  LINK RISK ANALYSIS\n");
//        analysisText.append("═══════════════════════════════════════════════════════\n\n");
//        analysisText.append("No links detected in this email.\n\n");
//    }
//
//    // Set details in the select view
//    dashboardSelectView.setEmailDetails(
//            email.getSender(),
//            email.getTitle(),
//            String.format("%.1f", email.getSuspicionScore()),
//            status,
//            dateStr
//    );
//    dashboardSelectView.setEmailText(emailText);
//    dashboardSelectView.setAnalysisText(analysisText.toString());
//
//    // Switch to the email details screen
//    viewManagerModel.setState(dashboardSelectView.getViewName());
//    viewManagerModel.firePropertyChange();
//}
//
//private boolean isLinkSuspicious(String url) {
//    if (url == null || url.trim().isEmpty()) return false;
//
//    String lowerUrl = url.toLowerCase();
//
//    return lowerUrl.contains("bit.ly") ||
//            lowerUrl.contains("tinyurl") ||
//            lowerUrl.contains("goo.gl") ||
//            lowerUrl.contains("ow.ly") ||
//            lowerUrl.contains("@") ||
//            lowerUrl.matches(".*\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*") ||
//            lowerUrl.contains("-login") ||
//            lowerUrl.contains("_login") ||
//            lowerUrl.contains("verify") ||
//            lowerUrl.contains("confirm") ||
//            lowerUrl.contains("secure") ||
//            lowerUrl.contains("account-update") ||
//            lowerUrl.matches(".*(paypa1|paypai|amazom|gogle|micr0soft|appleid-|bankofamerica-).*");
//}
//
//private String analyzeLinkRisk(String url) {
//    if (url == null || url.trim().isEmpty()) {
//        return "Empty URL";
//    }
//
//    String lowerUrl = url.toLowerCase();
//    StringBuilder analysis = new StringBuilder();
//    analysis.append("🔗 ").append(url).append("\n");
//
//    boolean isSuspicious = false;
//    java.util.List<String> warnings = new java.util.ArrayList<>();
//
//    // Check for URL shorteners
//    if (lowerUrl.contains("bit.ly") || lowerUrl.contains("tinyurl") ||
//            lowerUrl.contains("goo.gl") || lowerUrl.contains("ow.ly")) {
//        warnings.add("URL shortener (hides real destination)");
//        isSuspicious = true;
//    }
//
//    // Check for IP address instead of domain
//    if (lowerUrl.matches(".*\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*")) {
//        warnings.add("Uses IP address instead of domain name");
//        isSuspicious = true;
//    }
//
//    // Check for @ symbol (can hide real destination)
//    if (lowerUrl.contains("@")) {
//        warnings.add("Contains @ symbol (can hide destination)");
//        isSuspicious = true;
//    }
//
//    // Check for suspicious keywords
//    if (lowerUrl.contains("-login") || lowerUrl.contains("_login")) {
//        warnings.add("Suspicious login page pattern");
//        isSuspicious = true;
//    }
//    if (lowerUrl.contains("verify") || lowerUrl.contains("confirm")) {
//        warnings.add("Verification/confirmation page");
//        isSuspicious = true;
//    }
//    if (lowerUrl.contains("secure") || lowerUrl.contains("account-update")) {
//        warnings.add("Fake security/update page");
//        isSuspicious = true;
//    }
//
//    // Check for typosquatting
//    if (lowerUrl.matches(".*(paypa1|paypai|amazom|gogle|micr0soft|appleid-|bankofamerica-).*")) {
//        warnings.add("⚠️  TYPOSQUATTING DETECTED - Misspelled domain!");
//        isSuspicious = true;
//    }
//
//    // Build result
//    if (isSuspicious) {
//        analysis.append("   Risk Level: ⚠️  HIGH RISK\n");
//        analysis.append("   Reasons:\n");
//        for (String warning : warnings) {
//            analysis.append("     • ").append(warning).append("\n");
//        }
//    } else {
//        analysis.append("   Risk Level: ✓ Low Risk\n");
//        analysis.append("   This link appears to be safe.");
//    }
//
//    return analysis.toString();
//}
//}
//
