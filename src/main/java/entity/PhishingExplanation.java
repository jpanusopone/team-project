package entity;

import java.util.List;

/**
 * Represents the explanation generated for a phishing assessment.
 * Contains risk classification, indicators, reasons, and suggested actions.
 */
public class PhishingExplanation {

    private final boolean suspicious;
    private final RiskLevel riskLevel;
    private final List<String> reasons;
    private final PhishingIndicators indicators;
    private final List<String> suggestedActions;

    /**
     * Constructs a phishing explanation with all relevant details.
     *
     * @param suspicious whether the email is suspicious
     * @param riskLevel the assessed risk level
     * @param reasons list of reasons for the assessment
     * @param indicators phishing indicators found in the email
     * @param suggestedActions recommended user actions
     */
    public PhishingExplanation(boolean suspicious,
                               RiskLevel riskLevel,
                               List<String> reasons,
                               PhishingIndicators indicators,
                               List<String> suggestedActions) {
        this.suspicious = suspicious;
        this.riskLevel = riskLevel;
        this.reasons = reasons;
        this.indicators = indicators;
        this.suggestedActions = suggestedActions;
    }

    /**
     * Returns whether the email is suspicious.
     *
     * @return true if the email is suspicious
     */
    public boolean isSuspicious() {
        return suspicious;
    }

    /**
     * Returns the assessed risk level.
     *
     * @return the risk level for this explanation
     */
    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    /**
     * Returns the list of reasons for the phishing assessment.
     *
     * @return list of reasons describing why the email was flagged
     */
    public List<String> getReasons() {
        return reasons;
    }

    /**
     * Returns phishing indicators found in the email.
     *
     * @return phishing indicators associated with this explanation
     */
    public PhishingIndicators getIndicators() {
        return indicators;
    }

    /**
     * Returns the recommended actions for the user.
     *
     * @return list of suggested actions for handling the email
     */
    public List<String> getSuggestedActions() {
        return suggestedActions;
    }
}
