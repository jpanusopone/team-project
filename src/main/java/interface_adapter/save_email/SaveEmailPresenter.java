package interface_adapter.save_email;

import use_case.save_email.SaveEmailOutputBoundary;
import use_case.save_email.SaveEmailOutputData;

/**
 * Presenter for SaveEmail use case.
 * Formats output data for the view.
 */
public class SaveEmailPresenter implements SaveEmailOutputBoundary {
    private SaveEmailViewModel viewModel;

    public SaveEmailPresenter(SaveEmailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(SaveEmailOutputData outputData) {
        viewModel.setSuccess(true);
        viewModel.setMessage(outputData.getMessage());
        viewModel.firePropertyChange();
    }

    @Override
    public void presentError(SaveEmailOutputData outputData) {
        viewModel.setSuccess(false);
        viewModel.setMessage(outputData.getMessage());
        viewModel.firePropertyChange();
    }
}
