package presentation;

import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExplanationControllerTest {

    @Mock
    private ExplainPhishingEmailUseCase mockUseCase;

    private ExplanationController controller;
    private PhishingExplanation mockExplanation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ExplanationController(mockUseCase);

        // Create a mock explanation for testing
        PhishingIndicators indicators = new PhishingIndicators(
                Collections.singletonList("http://suspicious-link.com"),
                "phisher@example.com",
                "different@example.com",
                true,
                true,
                true,
                Arrays.asList("malware.exe")
        );

        mockExplanation = new PhishingExplanation(
                true,
                RiskLevel.HIGH,
                Arrays.asList("Suspicious URL detected", "Requests sensitive information", "Display name mismatch"),
                indicators,
                Arrays.asList("Do not click any links", "Report this email", "Delete the email")
        );
    }

    @Test
    void testGetExplanationSuccess() throws ExplanationException {
        String emailContent = "This is a suspicious email";
        when(mockUseCase.execute(emailContent)).thenReturn(mockExplanation);

        ExplanationResponse response = controller.getExplanation(emailContent);

        assertTrue(response.isSuccess());
        assertNotNull(response.getExplanation());
        assertEquals(mockExplanation, response.getExplanation());
        assertNull(response.getErrorMessage());
        verify(mockUseCase, times(1)).execute(emailContent);
    }

    @Test
    void testGetExplanationFailure() throws ExplanationException {
        String emailContent = "This is a test email";
        String errorMessage = "All explanation services failed";
        when(mockUseCase.execute(emailContent)).thenThrow(new ExplanationException(errorMessage));

        ExplanationResponse response = controller.getExplanation(emailContent);

        assertFalse(response.isSuccess());
        assertNull(response.getExplanation());
        assertEquals(errorMessage, response.getErrorMessage());
        verify(mockUseCase, times(1)).execute(emailContent);
    }

    @Test
    void testGetExplanationWithEmptyContent() throws ExplanationException {
        String emailContent = "";
        String errorMessage = "Email content cannot be empty";
        when(mockUseCase.execute(emailContent)).thenThrow(new ExplanationException(errorMessage));

        ExplanationResponse response = controller.getExplanation(emailContent);

        assertFalse(response.isSuccess());
        assertNull(response.getExplanation());
        assertEquals(errorMessage, response.getErrorMessage());
        verify(mockUseCase, times(1)).execute(emailContent);
    }

    @Test
    void testGetExplanationVerifiesExplanationContent() throws ExplanationException {
        String emailContent = "Test email with phishing indicators";
        when(mockUseCase.execute(emailContent)).thenReturn(mockExplanation);

        ExplanationResponse response = controller.getExplanation(emailContent);

        assertTrue(response.isSuccess());
        PhishingExplanation explanation = response.getExplanation();

        // Verify explanation details
        assertTrue(explanation.isSuspicious());
        assertEquals(RiskLevel.HIGH, explanation.getRiskLevel());
        assertEquals(3, explanation.getReasons().size());
        assertEquals(3, explanation.getSuggestedActions().size());

        // Verify indicators
        PhishingIndicators indicators = explanation.getIndicators();
        assertEquals(1, indicators.getUrls().size());
        assertEquals("http://suspicious-link.com", indicators.getUrls().get(0));
        assertEquals("phisher@example.com", indicators.getSender());
        assertEquals("different@example.com", indicators.getReplyTo());
        assertTrue(indicators.getDisplayNameMismatch());
        assertTrue(indicators.getUrgentLanguage());
        assertTrue(indicators.getRequestsSensitiveInfo());
        assertEquals(1, indicators.getAttachments().size());
        assertEquals("malware.exe", indicators.getAttachments().get(0));
    }
}
