package entity;

import java.util.List;

public class PhishingExplanation {
    private final boolean suspicious;
    private final RiskLevel riskLevel;
    private final List<String> reasons;
    private final PhishingIndicators indicators;
    private final List<String> suggestedActions;

    public PhishingExplanation(boolean suspicious, RiskLevel riskLevel,
                               List<String> reasons, PhishingIndicators indicators,
                               List<String> suggestedActions) {
        this.suspicious = suspicious;
        this.riskLevel = riskLevel;
        this.reasons = reasons;
        this.indicators = indicators;
        this.suggestedActions = suggestedActions;
    }

    // Getters
    public boolean isSuspicious() { return suspicious; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public List<String> getReasons() { return reasons; }
    public PhishingIndicators getIndicators() { return indicators; }
    public List<String> getSuggestedActions() { return suggestedActions; }
}