package interface_adapter.link_risk;

import use_case.link_risk.LinkRiskInputBoundary;
import use_case.link_risk.LinkRiskInputData;

/**
 * Controller for the Link Risk use case.
 * Converts view input into use-case input data.
 */
public class LinkRiskController {

    private final LinkRiskInputBoundary linkRiskInteractor;

    /**
     * Constructs a LinkRiskController.
     *
     * @param linkRiskInteractor the interactor to call
     */
    public LinkRiskController(LinkRiskInputBoundary linkRiskInteractor) {
        this.linkRiskInteractor = linkRiskInteractor;
    }

    /**
     * Executes link risk analysis on the provided email content.
     *
     * @param emailText the email text containing links to analyze
     */
    public void execute(String emailText) {
        final LinkRiskInputData inputData = new LinkRiskInputData(emailText);
        linkRiskInteractor.execute(inputData);
    }
}
