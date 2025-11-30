package interface_adapter.link_risk;

import use_case.link_risk.LinkRiskInputBoundary;
import use_case.link_risk.LinkRiskInputData;

/**
 * Controller for Link Risk use case.
 * Converts view input to use case input.
 */
public class LinkRiskController {

    private final LinkRiskInputBoundary linkRiskInteractor;

    public LinkRiskController(LinkRiskInputBoundary linkRiskInteractor) {
        this.linkRiskInteractor = linkRiskInteractor;
    }

    /**
     * Execute link risk analysis on email text.
     * @param emailText the email content to extract and analyze URLs from
     */
    public void execute(String emailText) {
        LinkRiskInputData inputData = new LinkRiskInputData(emailText);
        linkRiskInteractor.execute(inputData);
    }
}
