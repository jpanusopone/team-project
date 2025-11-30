package interface_adapter.link_risk;

import interface_adapter.ViewModel;

/**
 * View Model for Link Risk analysis.
 */
public class LinkRiskViewModel extends ViewModel<LinkRiskState> {

    public LinkRiskViewModel() {
        super("linkRisk");
        setState(new LinkRiskState());
    }
}
