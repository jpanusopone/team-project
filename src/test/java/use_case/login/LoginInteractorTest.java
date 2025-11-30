package use_case.login;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoginInteractor only.
 * No Swing, no ViewManagerModel here.
 */
class LoginInteractorTest {

    // Simple fake presenter we can assert on
    private static class FakePresenter implements LoginOutputBoundary {

        boolean successCalled = false;
        boolean failCalled = false;
        LoginOutputData successData = null;
        String failMessage = null;

        @Override
        public void prepareSuccessView(LoginOutputData outputData) {
            successCalled = true;
            successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            failMessage = errorMessage;
        }
    }

    @Test
    void execute_withCorrectCredentials_callsSuccess() {
        // Arrange
        FakePresenter presenter = new FakePresenter();
        LoginInteractor interactor = new LoginInteractor(presenter);

        LoginInputData input =
                new LoginInputData("username", "password"); // <-- the correct ones

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(presenter.successCalled, "Success should be called");
        assertFalse(presenter.failCalled, "Fail should not be called");
        assertNotNull(presenter.successData, "Success data should not be null");
        assertEquals("username", presenter.successData.getUsername());
    }

    @Test
    void execute_withWrongCredentials_callsFail() {
        // Arrange
        FakePresenter presenter = new FakePresenter();
        LoginInteractor interactor = new LoginInteractor(presenter);

        LoginInputData input =
                new LoginInputData("wrongUser", "wrongPass");

        // Act
        interactor.execute(input);

        // Assert
        assertTrue(presenter.failCalled, "Fail should be called");
        assertFalse(presenter.successCalled, "Success should not be called");
        assertEquals("Username and password are wrong.", presenter.failMessage);
    }
}
