package use_case.link_risk;

import java.util.List;

public class LinkRiskOutputData {
    private final List<String> urls;
    private final List<String> riskLevels;

    public LinkRiskOutputData(List<String> urls, List<String> riskLevels) {
        this.urls = urls;
        this.riskLevels = riskLevels;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getRiskLevels() {
        return riskLevels;
    }
}
