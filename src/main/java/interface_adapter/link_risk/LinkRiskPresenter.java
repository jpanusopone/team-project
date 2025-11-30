package interface_adapter.link_risk;

import use_case.link_risk.LinkRiskOutputBoundary;
import use_case.link_risk.LinkRiskOutputData;

/**
 * Presenter for Link Risk use case.
 * Converts use case output to view model state.
 */
public class LinkRiskPresenter implements LinkRiskOutputBoundary {

    private final LinkRiskViewModel linkRiskViewModel;

    public LinkRiskPresenter(LinkRiskViewModel linkRiskViewModel) {
        this.linkRiskViewModel = linkRiskViewModel;
    }

    @Override
    public void prepareSuccessView(LinkRiskOutputData outputData) {
        LinkRiskState state = new LinkRiskState(
                outputData.getUrls(),
                outputData.getRiskLevels()
        );
        linkRiskViewModel.setState(state);
        linkRiskViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String error) {
        LinkRiskState state = new LinkRiskState();
        state.setError(error);
        linkRiskViewModel.setState(state);
        linkRiskViewModel.firePropertyChange();
    }
}
