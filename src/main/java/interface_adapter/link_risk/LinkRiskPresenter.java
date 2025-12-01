package interface_adapter.link_risk;

import use_case.link_risk.LinkRiskOutputBoundary;
import use_case.link_risk.LinkRiskOutputData;

/**
 * Presenter for the Link Risk use case.
 * Converts use-case output into view-model state updates.
 */
public class LinkRiskPresenter implements LinkRiskOutputBoundary {

    private final LinkRiskViewModel linkRiskViewModel;

    /**
     * Constructs a presenter for the Link Risk use case.
     *
     * @param linkRiskViewModel the ViewModel to update
     */
    public LinkRiskPresenter(LinkRiskViewModel linkRiskViewModel) {
        this.linkRiskViewModel = linkRiskViewModel;
    }

    @Override
    public void prepareSuccessView(LinkRiskOutputData outputData) {
        final LinkRiskState state = new LinkRiskState(
                outputData.getUrls(),
                outputData.getRiskLevels()
        );
        linkRiskViewModel.setState(state);
        linkRiskViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String error) {
        final LinkRiskState state = new LinkRiskState();
        state.setError(error);
        linkRiskViewModel.setState(state);
        linkRiskViewModel.firePropertyChange();
    }
}
