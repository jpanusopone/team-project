package use_case.link_risk;

import java.util.List;

public class LinkRiskOutputData {
    private final List<String> urls;
    private final List<String> riskLevels;

    private final boolean apiError;
    private final String errorMessage;

    public LinkRiskOutputData(List<String> urls, List<String> riskLevels, boolean apiError, String errorMessage) {
        this.urls = urls;
        this.riskLevels = riskLevels;
        this.apiError = apiError;
        this.errorMessage = errorMessage;
    }

    public static LinkRiskOutputData apiFailure(String  errorMessage) {
        return new LinkRiskOutputData(
                List.of(), List.of(), true, errorMessage
        );
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getRiskLevels() {
        return riskLevels;
    }

    public Boolean getApiError() {
        return apiError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
