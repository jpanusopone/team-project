package use_case.link_risk;

public class LinkRiskResult {

    private final String url;
    private final String riskLevel;   // e.g. "SAFE", "DANGEROUS", "UNKNOWN"

    public LinkRiskResult(String url, String riskLevel) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url must not be null or blank");
        }
        if (riskLevel == null || riskLevel.isBlank()) {
            throw new IllegalArgumentException("riskLevel must not be null or blank");
        }

        this.url = url;
        this.riskLevel = riskLevel;
    }

    public String getUrl() {
        return url;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    @Override
    public String toString() {
        return "LinkRiskResult{" +
                "url='" + url + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LinkRiskResult)) return false;

        LinkRiskResult that = (LinkRiskResult) obj;

        if (!url.equals(that.url)) return false;
        return riskLevel.equals(that.riskLevel);
    }

    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = 31 * result + riskLevel.hashCode();
        return result;
    }
}