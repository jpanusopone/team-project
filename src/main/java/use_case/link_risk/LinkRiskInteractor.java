package use_case.link_risk;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interactor for the Link Risk use case.
 * Coordinates URL extraction, risk checking, and presenter output.
 */
public class LinkRiskInteractor implements LinkRiskInputBoundary {

    /**
     * Data access interface used to check URL risk.
     */
    private final LinkRiskUserDataAccessInterface dataAccess;

    /**
     * Output boundary (presenter) for preparing the view model.
     */
    private final LinkRiskOutputBoundary outputBoundary;

    /**
     * Construct a LinkRiskInteractor with the given data access and output boundary.
     *
     * @param dataAccess      the data access interface for checking URL risk
     * @param outputBoundary  the output boundary for presenting results
     */
    public LinkRiskInteractor(LinkRiskUserDataAccessInterface dataAccess,
                              LinkRiskOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(LinkRiskInputData inputData) {
        final String emailText = inputData.getEmailText();
        final List<String> urls = UrlExtractor.extractUrls(emailText);

        if (urls.isEmpty()) {
            final LinkRiskOutputData outputData =
                    new LinkRiskOutputData(List.of(), List.of());
            outputBoundary.prepareSuccessView(outputData);
        }
        else {
            try {
                final List<LinkRiskResult> results = dataAccess.checkUrls(urls);

                // Convert results into plain strings for OutputData
                final List<String> riskLevels = results.stream()
                        .map(LinkRiskResult::getRiskLevel)
                        .collect(Collectors.toList());

                final LinkRiskOutputData outputData =
                        new LinkRiskOutputData(urls, riskLevels);

                outputBoundary.prepareSuccessView(outputData);
            }
            catch (LinkRiskApiException ex) {
                outputBoundary.prepareFailureView(
                        "Unable to analyze links right now. Please try again later."
                );
            }
        }
    }
}
