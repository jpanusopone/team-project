package use_case.link_risk;

public interface LinkRiskOutputBoundary {
    void prepareSuccessView(LinkRiskOutputData linkRiskOutputData);

    void prepareFailureView(String errorMessage);
}
