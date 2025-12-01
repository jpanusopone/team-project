package use_case.link_risk;

/**
 * Input boundary for the Link Risk use case.
 * Defines the method that controllers must call
 * to initiate link risk analysis.
 */
public interface LinkRiskInputBoundary {

    /**
     * Execute the link risk analysis with the given input data.
     *
     * @param linkRiskInputData the input data containing email text or URLs
     */
    void execute(LinkRiskInputData linkRiskInputData);
}
