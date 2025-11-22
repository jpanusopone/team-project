package use_case.link_risk;

import java.util.List;
import java.util.stream.Collectors;

public class LinkRiskInteractor implements LinkRiskInputBoundary {
    private final LinkRiskUserDataAccessInterface dataAccess;   // Data Access Interface (red box)
    private final LinkRiskOutputBoundary outputBoundary;    // Output Boundary (Presenter)

    public LinkRiskInteractor(LinkRiskUserDataAccessInterface dataAccess, LinkRiskOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(LinkRiskInputData inputData) {
        String emailText = inputData.getEmailText();

        List<String> urls = UrlExtractor.extractUrls(emailText);

        if (urls.isEmpty()) {
            LinkRiskOutputData outputData = new LinkRiskOutputData(List.of(), List.of());
            outputBoundary.prepareSuccessView(outputData);
            return;
        }

        try {
            List<LinkRiskResult> results = dataAccess.checkUrls(urls);

            // Convert results into plain strings for OutputData
            List<String> riskLevels = results.stream()
                    .map(LinkRiskResult::getRiskLevel)
                    .collect(Collectors.toList());

            LinkRiskOutputData outputData = new LinkRiskOutputData(
                    urls,
                    riskLevels
            );

            outputBoundary.prepareSuccessView(outputData);

        } catch (LinkRiskApiException e) {
            outputBoundary.prepareFailureView(
                    "Unable to analyze links right now. Please try again later."
            );
        }
    }
}
