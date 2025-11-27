package usecase;

import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import use_case.ExplainPhishingEmailUseCase;
import use_case.interfaces.ExplanationException;
import use_case.interfaces.ExplanationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExplainPhishingEmailUseCaseTest {

    @Mock
    private ExplanationService mockService1;

    @Mock
    private ExplanationService mockService2;

    @Mock
    private ExplanationService mockService3;

    private ExplainPhishingEmailUseCase useCase;
    private PhishingExplanation mockExplanation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a mock explanation for testing
        PhishingIndicators indicators = new PhishingIndicators(
                Collections.singletonList("http://suspicious-link.com"),
                "phisher@example.com",
                null,
                false,
                true,
                true,
                Collections.emptyList()
        );

        mockExplanation = new PhishingExplanation(
                true,
                RiskLevel.HIGH,
                Arrays.asList("Suspicious URL detected", "Requests sensitive information"),
                indicators,
                Arrays.asList("Do not click any links", "Report this email")
        );
    }

    @Test
    void testExecuteWithEmptyEmail() throws Exception {
        useCase = new ExplainPhishingEmailUseCase(Collections.singletonList(mockService1));

        ExplanationException exception = assertThrows(ExplanationException.class, () -> {
            useCase.execute("");
        });

        assertEquals("Email content cannot be empty", exception.getMessage());
        verify(mockService1, never()).explainEmail(anyString());
    }

    @Test
    void testExecuteWithNullEmail() throws Exception {
        useCase = new ExplainPhishingEmailUseCase(Collections.singletonList(mockService1));

        ExplanationException exception = assertThrows(ExplanationException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("Email content cannot be empty", exception.getMessage());
        verify(mockService1, never()).explainEmail(anyString());
    }

    @Test
    void testExecuteSuccessWithFirstService() throws Exception {
        when(mockService1.explainEmail(anyString())).thenReturn(mockExplanation);

        useCase = new ExplainPhishingEmailUseCase(Collections.singletonList(mockService1));

        String emailContent = "This is a test email";
        PhishingExplanation result = useCase.execute(emailContent);

        assertNotNull(result);
        assertEquals(mockExplanation, result);
        verify(mockService1, times(1)).explainEmail(emailContent);
    }

    @Test
    void testFallbackToSecondServiceWhenFirstFails() throws Exception {
        when(mockService1.explainEmail(anyString())).thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(anyString())).thenReturn(mockExplanation);

        useCase = new ExplainPhishingEmailUseCase(Arrays.asList(mockService1, mockService2));

        String emailContent = "This is a test email";
        PhishingExplanation result = useCase.execute(emailContent);

        assertNotNull(result);
        assertEquals(mockExplanation, result);
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
    }

    @Test
    void testFallbackToThirdServiceWhenFirstTwoFail() throws Exception {
        when(mockService1.explainEmail(anyString())).thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(anyString())).thenThrow(new ExplanationException("Service 2 failed"));
        when(mockService3.explainEmail(anyString())).thenReturn(mockExplanation);

        useCase = new ExplainPhishingEmailUseCase(Arrays.asList(mockService1, mockService2, mockService3));

        String emailContent = "This is a test email";
        PhishingExplanation result = useCase.execute(emailContent);

        assertNotNull(result);
        assertEquals(mockExplanation, result);
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
        verify(mockService3, times(1)).explainEmail(emailContent);
    }

    @Test
    void testAllServicesFail() throws Exception {
        when(mockService1.explainEmail(anyString())).thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(anyString())).thenThrow(new ExplanationException("Service 2 failed"));
        when(mockService3.explainEmail(anyString())).thenThrow(new ExplanationException("Service 3 failed"));

        useCase = new ExplainPhishingEmailUseCase(Arrays.asList(mockService1, mockService2, mockService3));

        String emailContent = "This is a test email";

        ExplanationException exception = assertThrows(ExplanationException.class, () -> {
            useCase.execute(emailContent);
        });

        assertEquals("All explanation services failed", exception.getMessage());
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
        verify(mockService3, times(1)).explainEmail(emailContent);
    }

    @Test
    void testExecuteWithWhitespaceEmail() throws Exception {
        useCase = new ExplainPhishingEmailUseCase(Collections.singletonList(mockService1));

        ExplanationException exception = assertThrows(ExplanationException.class, () -> {
            useCase.execute("   ");
        });

        assertEquals("Email content cannot be empty", exception.getMessage());
        verify(mockService1, never()).explainEmail(anyString());
    }
}
