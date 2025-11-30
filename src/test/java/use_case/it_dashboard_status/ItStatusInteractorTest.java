// test/use_case/it_dashboard_status/ItUpdateStatusInteractorTest.java
package use_case.it_dashboard_status;

import data_access.ItVerificationGateway;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ItUpdateStatusInteractorTest {

    // --- Fake DAO for tests ---
    private static class FakeDao implements ItVerificationGateway {
        String lastId;
        String lastStatus;
        boolean shouldThrow = false;

        @Override
        public void updateEmailVerificationStatus(String emailId, String status)
                throws ExecutionException, InterruptedException {
            if (shouldThrow) {
                throw new ExecutionException("boom", new RuntimeException());
            }
            this.lastId = emailId;
            this.lastStatus = status;
        }
    }

    // --- Fake presenter for tests ---
    private static class FakePresenter implements ItUpdateStatusOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        String status;
        String error;

        @Override
        public void prepareSuccessView(ItUpdateStatusOutputData data) {
            successCalled = true;
            status = data.getStatus();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            error = errorMessage;
        }
    }

    @Test
    void updateStatus_success_callsDaoAndSuccessPresenter() {
        FakeDao dao = new FakeDao();
        FakePresenter presenter = new FakePresenter();
        ItUpdateStatusInteractor interactor = new ItUpdateStatusInteractor(dao, presenter);

        ItUpdateStatusInputData input =
                new ItUpdateStatusInputData("doc123", "Confirmed");

        interactor.updateStatus(input);

        assertEquals("doc123", dao.lastId);
        assertEquals("Confirmed", dao.lastStatus);

        assertTrue(presenter.successCalled);
        assertEquals("Confirmed", presenter.status);
        assertFalse(presenter.failCalled);
    }

    @Test
    void updateStatus_failure_callsFailPresenter() {
        FakeDao dao = new FakeDao();
        dao.shouldThrow = true;

        FakePresenter presenter = new FakePresenter();
        ItUpdateStatusInteractor interactor = new ItUpdateStatusInteractor(dao, presenter);

        ItUpdateStatusInputData input =
                new ItUpdateStatusInputData("doc123", "Safe");

        interactor.updateStatus(input);

        assertTrue(presenter.failCalled);
        assertFalse(presenter.successCalled);
        assertNotNull(presenter.error);
        assertTrue(presenter.error.startsWith("Failed to update email status: "));
    }
}
