package use_case;

import entity.PhishingExplanation;
import entity.PhishingIndicators;
import entity.RiskLevel;
import org.junit.jupiter.api.Test;
import use_case.explain_phishing.ExplainPhishingInputData;
import use_case.explain_phishing.ExplainPhishingInteractor;
import use_case.explain_phishing.ExplainPhishingOutputData;
import use_case.interfaces.ExplanationException;
import use_case.interfaces.ExplanationService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ExplainPhishingInteractor.
 */
public class ExplainPhishingInteractorTest {

    @Test
    public void testSuccessfulExplanationFirstService() throws ExplanationException {
        // 1. Create mock services
        ExplanationService mockService1 = mock(ExplanationService.class);
        ExplanationService mockService2 = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService1, mockService2);

        // 2. Create test input
        String emailContent = "Click here to verify your account immediately!";
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);

        // 3. Create expected explanation
        PhishingIndicators indicators = new PhishingIndicators(
                Arrays.asList("http://suspicious.com"),
                "scammer@fake.com",
                "different@email.com",
                true,
                true,
                true,
                Arrays.asList()
        );
        PhishingExplanation expectedExplanation = new PhishingExplanation(
                true,
                RiskLevel.HIGH,
                Arrays.asList("Urgent language detected", "Requests sensitive info"),
                indicators,
                Arrays.asList("Do not click links", "Verify sender")
        );

        // 4. Configure mock to return explanation
        when(mockService1.explainEmail(emailContent)).thenReturn(expectedExplanation);

        // 5. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 6. Verify results
        assertTrue(output.isSuccess());
        assertNotNull(output.getExplanation());
        assertEquals(expectedExplanation, output.getExplanation());
        assertNull(output.getErrorMessage());

        // 7. Verify only first service was called
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, never()).explainEmail(anyString());
    }

    @Test
    public void testFallbackToSecondService() throws ExplanationException {
        // 1. Create mock services
        ExplanationService mockService1 = mock(ExplanationService.class);
        ExplanationService mockService2 = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService1, mockService2);

        // 2. Create test input
        String emailContent = "Test email content";
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);

        // 3. Create expected explanation
        PhishingIndicators indicators = new PhishingIndicators(
                Arrays.asList(),
                "normal@email.com",
                "normal@email.com",
                false,
                false,
                false,
                Arrays.asList()
        );
        PhishingExplanation expectedExplanation = new PhishingExplanation(
                false,
                RiskLevel.LOW,
                Arrays.asList("No suspicious indicators found"),
                indicators,
                Arrays.asList("Email appears safe")
        );

        // 4. Configure first service to throw exception, second to succeed
        when(mockService1.explainEmail(emailContent))
                .thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(emailContent)).thenReturn(expectedExplanation);

        // 5. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 6. Verify results
        assertTrue(output.isSuccess());
        assertNotNull(output.getExplanation());
        assertEquals(expectedExplanation, output.getExplanation());
        assertNull(output.getErrorMessage());

        // 7. Verify both services were called in order
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
    }

    @Test
    public void testAllServicesFail() throws ExplanationException {
        // 1. Create mock services
        ExplanationService mockService1 = mock(ExplanationService.class);
        ExplanationService mockService2 = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService1, mockService2);

        // 2. Create test input
        String emailContent = "Test email content";
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);

        // 3. Configure both services to fail
        when(mockService1.explainEmail(emailContent))
                .thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(emailContent))
                .thenThrow(new ExplanationException("Service 2 failed"));

        // 4. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 5. Verify failure
        assertFalse(output.isSuccess());
        assertNull(output.getExplanation());
        assertEquals("All explanation services failed", output.getErrorMessage());

        // 6. Verify both services were attempted
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
    }

    @Test
    public void testEmptyEmailContent() {
        // 1. Create mock service
        ExplanationService mockService = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService);

        // 2. Create input with empty content
        ExplainPhishingInputData inputData = new ExplainPhishingInputData("");

        // 3. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 4. Verify validation error
        assertFalse(output.isSuccess());
        assertNull(output.getExplanation());
        assertEquals("Email content cannot be empty", output.getErrorMessage());

        // 5. Verify no service was called
        verifyNoInteractions(mockService);
    }

    @Test
    public void testWhitespaceOnlyEmailContent() {
        // 1. Create mock service
        ExplanationService mockService = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService);

        // 2. Create input with whitespace only
        ExplainPhishingInputData inputData = new ExplainPhishingInputData("   \t\n  ");

        // 3. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 4. Verify validation error
        assertFalse(output.isSuccess());
        assertNull(output.getExplanation());
        assertEquals("Email content cannot be empty", output.getErrorMessage());

        // 5. Verify no service was called
        verifyNoInteractions(mockService);
    }

    @Test
    public void testNullEmailContent() {
        // 1. Create mock service
        ExplanationService mockService = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService);

        // 2. Create input with null content
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(null);

        // 3. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 4. Verify validation error
        assertFalse(output.isSuccess());
        assertNull(output.getExplanation());
        assertEquals("Email content cannot be empty", output.getErrorMessage());

        // 5. Verify no service was called
        verifyNoInteractions(mockService);
    }

    @Test
    public void testFallbackWithMultipleServices() throws ExplanationException {
        // 1. Create three mock services
        ExplanationService mockService1 = mock(ExplanationService.class);
        ExplanationService mockService2 = mock(ExplanationService.class);
        ExplanationService mockService3 = mock(ExplanationService.class);
        List<ExplanationService> services = Arrays.asList(mockService1, mockService2, mockService3);

        // 2. Create test input
        String emailContent = "Suspicious email";
        ExplainPhishingInputData inputData = new ExplainPhishingInputData(emailContent);

        // 3. Create expected explanation
        PhishingIndicators indicators = new PhishingIndicators(
                Arrays.asList("http://phishing.com"),
                "bad@actor.com",
                "bad@actor.com",
                false,
                true,
                true,
                Arrays.asList()
        );
        PhishingExplanation expectedExplanation = new PhishingExplanation(
                true,
                RiskLevel.MEDIUM,
                Arrays.asList("Suspicious URL detected"),
                indicators,
                Arrays.asList("Be cautious")
        );

        // 4. Configure first two services to fail, third to succeed
        when(mockService1.explainEmail(emailContent))
                .thenThrow(new ExplanationException("Service 1 failed"));
        when(mockService2.explainEmail(emailContent))
                .thenThrow(new ExplanationException("Service 2 failed"));
        when(mockService3.explainEmail(emailContent)).thenReturn(expectedExplanation);

        // 5. Execute interactor
        ExplainPhishingInteractor interactor = new ExplainPhishingInteractor(services);
        ExplainPhishingOutputData output = interactor.execute(inputData);

        // 6. Verify results
        assertTrue(output.isSuccess());
        assertNotNull(output.getExplanation());
        assertEquals(expectedExplanation, output.getExplanation());

        // 7. Verify all three services were called in order
        verify(mockService1, times(1)).explainEmail(emailContent);
        verify(mockService2, times(1)).explainEmail(emailContent);
        verify(mockService3, times(1)).explainEmail(emailContent);
    }
}
