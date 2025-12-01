package interface_adapter.explain_phishing;

import use_case.explain_phishing.ExplainPhishingOutputBoundary;
import use_case.explain_phishing.ExplainPhishingOutputData;

/**
 * ExplainPhishing Presenter.
 *
 * <p>Responsibility: Convert use case output into view-friendly state and update the ViewModel.
 *
 * <p>Clean Architecture Layer: Interface Adapter Layer
 *
 * <p>Dependencies: Implements use case output boundary (inward dependency)
 */
public class ExplainPhishingPresenter implements ExplainPhishingOutputBoundary {

    private final ExplainPhishingViewModel viewModel;

    public ExplainPhishingPresenter(ExplainPhishingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(ExplainPhishingOutputData outputData) {
        final ExplainPhishingState state = new ExplainPhishingState(outputData.getExplanation());
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    @Override
    public void presentFailure(String errorMessage) {
        final ExplainPhishingState state = new ExplainPhishingState(errorMessage);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    public ExplainPhishingViewModel getViewModel() {
        return viewModel;
    }
}
