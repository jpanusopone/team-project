package use_case.link_risk;

/**
 * Represents the result of checking the risk level of a URL.
 */
public class LinkRiskResult {

    /**
     * Multiplier used in hashCode calculation.
     */
    private static final int HASH_MULTIPLIER = 31;

    private final String url;
    private final String riskLevel;

    /**
     * Creates a new {@code LinkRiskResult}.
     *
     * @param url       the URL that was evaluated
     * @param riskLevel the risk assessment (for example, SAFE, DANGEROUS)
     * @throws IllegalArgumentException if {@code url} or {@code riskLevel} is null or blank
     */
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

    /**
     * Returns the URL that was evaluated.
     *
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the risk level assigned to the URL.
     *
     * @return the risk level string
     */
    public String getRiskLevel() {
        return riskLevel;
    }

    /**
     * Returns a string representation of this result.
     *
     * @return a string describing the URL and its risk level
     */
    @Override
    public String toString() {
        return "LinkRiskResult{"
                + "url='" + url + '\''
                + ", riskLevel='" + riskLevel + '\''
                + '}';
    }

    /**
     * Compares this object for equality with another.
     *
     * @param obj the object to compare with
     * @return {@code true} if {@code obj} is a {@code LinkRiskResult} with the same URL and risk level,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (this == obj) {
            equal = true;
        }
        else if (obj instanceof LinkRiskResult) {
            final LinkRiskResult that = (LinkRiskResult) obj;
            equal = url.equals(that.url) && riskLevel.equals(that.riskLevel);
        }
        return equal;
    }

    /**
     * Returns a hash code for this result.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = url.hashCode();
        result = HASH_MULTIPLIER * result + riskLevel.hashCode();
        return result;
    }
}
