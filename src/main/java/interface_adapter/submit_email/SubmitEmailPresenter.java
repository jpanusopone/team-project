package interface_adapter.submit_email;

import use_case.submit_email.SubmitEmailOutputBoundary;
import use_case.submit_email.SubmitEmailOutputData;

public class SubmitEmailPresenter implements SubmitEmailOutputBoundary {

    private final SubmitEmailViewModel viewModel;

    public SubmitEmailPresenter(SubmitEmailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SubmitEmailOutputData outputData) {
        if (outputData.hasError()) {
            viewModel.setErrorMessage(outputData.getErrorMessage());
            viewModel.setScoreText("Score: —");
            viewModel.setExplanation("");
            viewModel.setTitle("");
            viewModel.setSender("");
        } else {
            viewModel.setErrorMessage(null);
            viewModel.setTitle(outputData.getTitle());
            viewModel.setSender(outputData.getSender());
            viewModel.setScoreText("Score: " + outputData.getScore() + "/100");
            viewModel.setExplanation(outputData.getExplanation());
        }
    }

    public SubmitEmailViewModel getViewModel() {
        return viewModel;
    }
}