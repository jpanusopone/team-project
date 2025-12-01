package use_case.link_risk;

import java.util.List;

/**
 * Data access interface for checking the risk level of URLs.
 * Implementations should connect to the appropriate API or service.
 */
public interface LinkRiskUserDataAccessInterface {

    /**
     * Checks the provided URLs and returns their risk assessment results.
     *
     * @param urls the list of URLs to analyze
     * @return a list of {@link LinkRiskResult} objects representing URL risk evaluations
     * @throws LinkRiskApiException if the external API or data source fails
     */
    List<LinkRiskResult> checkUrls(List<String> urls) throws LinkRiskApiException;
}
