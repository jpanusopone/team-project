package use_case.submit_email;

import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;
import use_case.submit_email.SubmitEmailInputData;
import use_case.submit_email.SubmitEmailInteractor;
import use_case.submit_email.SubmitEmailOutputBoundary;
import use_case.submit_email.SubmitEmailOutputData;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SubmitEmailInteractorTest {

    private SubmitEmailOutputBoundary presenter;
    private ExplainPhishingEmailUseCase explainPhishingEmailUseCase;
    private SubmitEmailInteractor interactor;

    @BeforeEach
    void setUp() {
        presenter = mock(SubmitEmailOutputBoundary.class);
        explainPhishingEmailUseCase = mock(ExplainPhishingEmailUseCase.class);
        interactor = new SubmitEmailInteractor(presenter, explainPhishingEmailUseCase);
    }

    @Test
    void testExecuteWithNullRawEmail() {
        // Arrange
        SubmitEmailInputData inputData = new SubmitEmailInputData(null);
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals("Please paste an email before analyzing.", output.getErrorMessage());
        assertNotNull(output.getErrorMessage());
    }

    @Test
    void testExecuteWithEmptyRawEmail() {
        // Arrange
        SubmitEmailInputData inputData = new SubmitEmailInputData("   ");
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals("Please paste an email before analyzing.", output.getErrorMessage());
    }

    @Test
    void testExecuteWithSuccessfulHighRiskAnalysis() throws ExplanationException {
        // Arrange
        String rawEmail = "From: scammer@evil.com\nClick here to claim your prize!";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        PhishingIndicators indicators = new PhishingIndicators(
                Arrays.asList("http://evil.com/phishing"),
                "scammer@evil.com",
                "noreply@evil.com",
                true,
                true,
                true,
                Collections.emptyList()
        );
        PhishingExplanation explanation = new PhishingExplanation(
                true,
                RiskLevel.HIGH,
                Arrays.asList("Contains suspicious link", "Unknown sender"),
                indicators,
                Arrays.asList("Do not click any links", "Report to IT")
        );

        when(explainPhishingEmailUseCase.execute(rawEmail)).thenReturn(explanation);
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        verify(explainPhishingEmailUseCase, times(1)).execute(rawEmail);
        SubmitEmailOutputData output = captor.getValue();
        assertEquals(100, output.getScore());
        assertNull(output.getErrorMessage());
        assertTrue(output.getExplanation().contains("Contains suspicious link"));
    }

    @Test
    void testExecuteWithSuccessfulMediumRiskAnalysis() throws ExplanationException {
        // Arrange
        String rawEmail = "From: unknown@company.com\nPlease verify your account";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        PhishingIndicators indicators = new PhishingIndicators(
                Collections.emptyList(),
                "unknown@company.com",
                null,
                false,
                true,
                false,
                Collections.emptyList()
        );
        PhishingExplanation explanation = new PhishingExplanation(
                true,
                RiskLevel.MEDIUM,
                Arrays.asList("Contains urgency language"),
                indicators,
                Arrays.asList("Verify sender identity")
        );

        when(explainPhishingEmailUseCase.execute(rawEmail)).thenReturn(explanation);
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals(50, output.getScore());
        assertNull(output.getErrorMessage());
    }

    @Test
    void testExecuteWithSuccessfulLowRiskAnalysis() throws ExplanationException {
        // Arrange
        String rawEmail = "From: colleague@company.com\nMeeting at 3pm";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        PhishingIndicators indicators = new PhishingIndicators(
                Collections.emptyList(),
                "colleague@company.com",
                "colleague@company.com",
                false,
                false,
                false,
                Collections.emptyList()
        );
        PhishingExplanation explanation = new PhishingExplanation(
                false,
                RiskLevel.LOW,
                Arrays.asList("Legitimate email"),
                indicators,
                Collections.emptyList()
        );

        when(explainPhishingEmailUseCase.execute(rawEmail)).thenReturn(explanation);
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals(20, output.getScore());
        assertNull(output.getErrorMessage());
    }

    @Test
    void testExecuteWithFailedAnalysisResponse() throws ExplanationException {
        // Arrange
        String rawEmail = "Test email";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        when(explainPhishingEmailUseCase.execute(rawEmail))
                .thenThrow(new ExplanationException("API error occurred"));
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertTrue(output.getErrorMessage().contains("Failed to analyze email"));
        assertTrue(output.getErrorMessage().contains("API error occurred"));
    }

    @Test
    void testExecuteWithIllegalStateException() throws ExplanationException {
        // Arrange
        String rawEmail = "Test email";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        when(explainPhishingEmailUseCase.execute(rawEmail))
                .thenThrow(new IllegalStateException("API key not set"));
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals("OPENAI_API_KEY is not set on this machine.", output.getErrorMessage());
    }

    @Test
    void testExecuteWithGenericException() throws ExplanationException {
        // Arrange
        String rawEmail = "Test email";
        SubmitEmailInputData inputData = new SubmitEmailInputData(rawEmail);

        when(explainPhishingEmailUseCase.execute(rawEmail))
                .thenThrow(new RuntimeException("Unexpected error"));
        ArgumentCaptor<SubmitEmailOutputData> captor = ArgumentCaptor.forClass(SubmitEmailOutputData.class);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter, times(1)).present(captor.capture());
        SubmitEmailOutputData output = captor.getValue();
        assertEquals("Unable to analyse this email. Please try again later.", output.getErrorMessage());
    }
}
