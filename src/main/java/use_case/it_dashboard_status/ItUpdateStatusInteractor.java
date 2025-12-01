// use_case/it_dashboard_status/ItUpdateStatusInteractor.java

package use_case.it_dashboard_status;

import java.util.concurrent.ExecutionException;

import data_access.ItVerificationGateway;

public class ItUpdateStatusInteractor implements ItUpdateStatusInputBoundary {

    private final ItVerificationGateway itDao;
    private final ItUpdateStatusOutputBoundary presenter;

    public ItUpdateStatusInteractor(ItVerificationGateway itDao,
                                    ItUpdateStatusOutputBoundary presenter) {
        this.itDao = itDao;
        this.presenter = presenter;
    }

    @Override
    public void updateStatus(ItUpdateStatusInputData inputData) {
        try {
            itDao.updateEmailVerificationStatus(inputData.getDocumentId(), inputData.getStatus());

            final ItUpdateStatusOutputData out =
                    new ItUpdateStatusOutputData(inputData.getStatus());
            presenter.prepareSuccessView(out);

        }
        catch (ExecutionException | InterruptedException ex) {
            presenter.prepareFailView("Failed to update email status: " + ex.getMessage());
        }
    }
}
