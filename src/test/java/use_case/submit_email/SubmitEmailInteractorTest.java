package use_case.submit_email;

import entity.Email;
import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import org.junit.jupiter.api.Test;
import presentation.ExplanationController;
import presentation.ExplanationResponse;
import use_case.submit_email.SubmitEmailInteractor;
import use_case.submit_email.SubmitEmailInputBoundary;
import use_case.submit_email.SubmitEmailOutputBoundary;
import use_case.submit_email.SubmitEmailInputData;
import use_case.submit_email.SubmitEmailOutputData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for SubmitEmailInteractor.
 * Goal: 100% code coverage
 */

public class SubmitEmailInteractorTest {

    // Simple presenter that just remembers the last output.
    private static class TestPresenter implements SubmitEmailOutputBoundary {
        private SubmitEmailOutputData last;

        @Override
        public void present(SubmitEmailOutputData data) {
            this.last = data;
        }
        public SubmitEmailOutputData getLast() {
            return last;
        }
    }

    // Fake controller where we fully control the ExplanationResponse.
    private static class FakeExplanationController extends ExplanationController {
        private final Function<String, ExplanationResponse> behavior;

        public FakeExplanationController(Function<String, ExplanationResponse> behavior) {
            super(null);
            this.behavior = behavior;
        }

        @Override
        public ExplanationResponse getExplanation(String emailContent) {
            return behavior.apply(emailContent);
        }
    }

    // Fake controller that always throws a specific RuntimeException
    private static class ThrowingExplanationController extends ExplanationController {

        private final RuntimeException toThrow;

        public ThrowingExplanationController(RuntimeException toThrow) {
            super(null);
            this.toThrow = toThrow;
        }

        @Override
        public ExplanationResponse getExplanation(String emailContent) {
            throw toThrow;
        }
    }

    // Helper to build a minimal PhishingExplanation.
    private PhishingExplanation makeExplanation(RiskLevel level) {
        List<String> reasons = List.of("reason1", "reason2");
        List<String> urls = List.of("http://example.com");
        PhishingIndicators indicators = new PhishingIndicators(
                urls,
                "sender@example.com",
                null,
                false,
                false,
                false,
                new ArrayList<>()
        );
        List<String> actions = List.of("delete email");

        return new PhishingExplanation(true, level, reasons, indicators, actions);
    }

    // -------------TESTS--------------

    @Test
    public void testBlankEmailShowsErrorAndSkipsController() {
        TestPresenter presenter = new TestPresenter();

        // if controller is called, we want test to fail
        ExplanationController controller = new FakeExplanationController(
                content -> { fail("Controller should not be called for blank email"); return null;}
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData( "  ");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("Please paste an email before analyzing.", out.getErrorMessage());
        assertEquals(0, out.getScore());
        assertEquals("", out.getTitle());
        assertEquals("", out.getSender());
    }

    @Test
    public void testApiFailureReturnsErrorMessage() {
        TestPresenter presenter = new TestPresenter();

        ExplanationController controller = new FakeExplanationController(
                content -> new ExplanationResponse(false, null, "Some API error")
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject: Hi\nFrom: a@b.com\n\nBody");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("Failed to analyze email: Some API error", out.getErrorMessage());
        assertEquals(0, out.getScore());
    }

    @Test
    public void testSuccessHighRiskBuildsEmailCorrectly() {
        TestPresenter presenter = new TestPresenter();

        PhishingExplanation explanation = makeExplanation(RiskLevel.HIGH);

        ExplanationController controller = new FakeExplanationController(
                content -> new ExplanationResponse(true, explanation, null)
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);
        String rawEmail = "Subject: Test Subject\nFrom: scammer@example.com\n\nHello, click http://example.com";
        SubmitEmailInputData input = new SubmitEmailInputData(rawEmail);
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);

        assertEquals("Test Subject", out.getTitle());
        assertEquals("scammer@example.com", out.getSender());

        assertEquals(100, out.getScore());

        assertEquals("reason1\nreason2", out.getExplanation());
        assertNull(out.getErrorMessage());
    }

    @Test
    public void testSuccesMediumRiskMapsToScore50() {
        TestPresenter presenter = new TestPresenter();

        PhishingExplanation explanation = makeExplanation(RiskLevel.MEDIUM);
        ExplanationController controller = new FakeExplanationController(
                content -> new ExplanationResponse(true, explanation, null)
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject: X\nFrom: y@z.com\n\nBody");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals(50, out.getScore());
    }

    @Test
    public void testSuccessLowRiskMapsToScore20() {
        TestPresenter presenter = new TestPresenter();

        PhishingExplanation explanation = makeExplanation(RiskLevel.LOW);
        ExplanationController controller = new FakeExplanationController(
                content -> new ExplanationResponse(true, explanation, null)
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject: X\nFrom:y@z.com\n\nBody");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals(20, out.getScore());
    }

    @Test
    public void testIllegalStateExceptionShowsApiKeyMessage() {
        TestPresenter presenter = new TestPresenter();

        ExplanationController controller = new ThrowingExplanationController(new IllegalStateException("no api key"));

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject:X\nFrom: y@z.com\n Body");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("OPENAI_API_KEY is not set on this machine.", out.getErrorMessage());
    }

    @Test
    public void testGenericExceptionShowsGenericErrorMessage() {
        TestPresenter presenter = new TestPresenter();

        ExplanationController controller = new ThrowingExplanationController(new RuntimeException("boom"));

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject: X\nFrom: Y@z.com\n\nBody");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("Unable to analyse this email. Please try again later.", out.getErrorMessage());
    }

    @Test
    public void testNullResponseTriggersGenericErrorBranch() {
        TestPresenter presenter = new TestPresenter();

        ExplanationController controller = new FakeExplanationController(
                content -> null
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData("Subject: X\nFrom: Y\n\nBody");
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("Unable to analyse this email. Please try again later.", out.getErrorMessage());
    }

    @Test
    public void testNullEmailShowsErrorAndSkipsController() {
        TestPresenter presenter = new TestPresenter();
        ExplanationController controller = new FakeExplanationController(
                content -> {
                    fail("Controller should not be called for null email");
                    return null;
                }
        );

        SubmitEmailInteractor interactor = new SubmitEmailInteractor(presenter, controller);

        SubmitEmailInputData input = new SubmitEmailInputData(null);
        interactor.execute(input);

        SubmitEmailOutputData out = presenter.getLast();
        assertNotNull(out);
        assertEquals("Please paste an email before analyzing.", out.getErrorMessage());
        assertEquals(0, out.getScore());
        assertEquals("", out.getTitle());
        assertEquals("", out.getSender());
    }

    @Test
    void testOutputDataBothBranches() {
        // --------- Case 1: No error (errorMessage = null) ----------
        SubmitEmailOutputData ok = new SubmitEmailOutputData(
                "Test Title",
                "sender@example.com",
                42,
                "Some explanation",
                null
        );

        assertEquals("Test Title", ok.getTitle());
        assertEquals("sender@example.com", ok.getSender());
        assertEquals(42, ok.getScore());
        assertEquals("Some explanation", ok.getExplanation());
        assertNull(ok.getErrorMessage());
        assertFalse(ok.hasError());  // tests branch 1


        // --------- Case 2: With error (errorMessage != null) ----------
        SubmitEmailOutputData err = new SubmitEmailOutputData(
                "",
                "",
                0,
                "",
                "Something went wrong"
        );

        assertEquals("Something went wrong", err.getErrorMessage());
        assertTrue(err.hasError());  // tests branch 2
    }
}
