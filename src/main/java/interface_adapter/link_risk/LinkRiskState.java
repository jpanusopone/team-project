package interface_adapter.link_risk;

import java.util.List;

/**
 * State for Link Risk analysis results.
 */
public class LinkRiskState {
    private List<String> urls;
    private List<String> riskLevels;
    private String error;

    public LinkRiskState() {
    }

    public LinkRiskState(List<String> urls, List<String> riskLevels) {
        this.urls = urls;
        this.riskLevels = riskLevels;
        this.error = null;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getRiskLevels() {
        return riskLevels;
    }

    public void setRiskLevels(List<String> riskLevels) {
        this.riskLevels = riskLevels;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
