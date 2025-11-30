package use_case.link_risk;

import java.util.List;

public interface LinkRiskUserDataAccessInterface {
    List<LinkRiskResult> checkUrls(List<String> urls) throws LinkRiskApiException;
}
