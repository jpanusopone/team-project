package use_case.link_risk;

/**
 * Output boundary for the Link Risk use case.
 * Defines methods for presenting success or failure results.
 */
public interface LinkRiskOutputBoundary {

    /**
     * Present a successful link risk analysis result.
     *
     * @param linkRiskOutputData the output data containing URLs and risk levels
     */
    void prepareSuccessView(LinkRiskOutputData linkRiskOutputData);

    /**
     * Present a failure result for the link risk analysis.
     *
     * @param errorMessage the error message to display
     */
    void prepareFailureView(String errorMessage);
}
