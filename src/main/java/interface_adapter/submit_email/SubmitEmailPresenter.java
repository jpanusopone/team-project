package interface_adapter.submit_email;

import use_case.submit_email.SubmitEmailOutputBoundary;
import use_case.submit_email.SubmitEmailOutputData;

/**
 * Presenter for the Submit Email use case.
 * Updates the {@link SubmitEmailViewModel} based on the output data.
 */
public class SubmitEmailPresenter implements SubmitEmailOutputBoundary {

    private final SubmitEmailViewModel viewModel;

    /**
     * Creates a presenter with the given view model.
     *
     * @param viewModel the view model to update
     */
    public SubmitEmailPresenter(SubmitEmailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SubmitEmailOutputData outputData) {
        if (outputData.hasError()) {
            viewModel.setErrorMessage(outputData.getErrorMessage());
            viewModel.setScoreText("Score: -");
            viewModel.setExplanation("");
            viewModel.setTitle("");
            viewModel.setSender("");
        }
        else {
            viewModel.setErrorMessage(null);
            viewModel.setTitle(outputData.getTitle());
            viewModel.setSender(outputData.getSender());
            viewModel.setScoreText("Score: " + outputData.getScore() + "/100");
            viewModel.setExplanation(outputData.getExplanation());
        }
    }

    /**
     * Returns the view model associated with this presenter.
     *
     * @return the submit email view model
     */
    public SubmitEmailViewModel getViewModel() {
        return viewModel;
    }
}
