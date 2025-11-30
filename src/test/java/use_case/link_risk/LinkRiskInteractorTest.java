package use_case.link_risk;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

class LinkRiskInteractorTest {

    private LinkRiskUserDataAccessInterface mockDataAccess;
    private LinkRiskOutputBoundary mockOutputBoundary;
    private LinkRiskInteractor interactor;

    @BeforeEach
    void setup() {
        mockDataAccess = mock(LinkRiskUserDataAccessInterface.class);
        mockOutputBoundary = mock(LinkRiskOutputBoundary.class);
        interactor = new LinkRiskInteractor(mockDataAccess, mockOutputBoundary);
    }

    @Test
    void execute_noUrls_shouldProduceEmptyLists() {
        // Arrange: email text with no URLs
        LinkRiskInputData input = new LinkRiskInputData("Hello, no links here!");

        // Act
        interactor.execute(input);

        // Assert
        @SuppressWarnings("unchecked")
        ArgumentCaptor<LinkRiskOutputData> captor = ArgumentCaptor.forClass(LinkRiskOutputData.class);
        verify(mockOutputBoundary, times(1)).prepareSuccessView(captor.capture());
        LinkRiskOutputData output = captor.getValue();

        assertNotNull(output);
        assertTrue(output.getUrls().isEmpty(), "Expected no URLs when email text has none");
        assertTrue(output.getRiskLevels().isEmpty(), "Expected no risk levels when no URLs");
        verifyNoInteractions(mockDataAccess);
        verify(mockOutputBoundary, never()).prepareFailureView(anyString());
    }

    @Test
    void execute_urlsPresent_allSafe_shouldMapSafeRiskLevels() throws Exception {
        String url1 = "http://example.com";
        String url2 = "https://sub.domain.org/path";
        LinkRiskInputData input = new LinkRiskInputData(
                "Visit " + url1 + " and " + url2
        );

        when(mockDataAccess.checkUrls(List.of(url1, url2)))
                .thenReturn(Arrays.asList(
                        new LinkRiskResult(url1, "SAFE"),
                        new LinkRiskResult(url2, "SAFE")
                ));

        interactor.execute(input);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<LinkRiskOutputData> captor = ArgumentCaptor.forClass(LinkRiskOutputData.class);
        verify(mockOutputBoundary, times(1)).prepareSuccessView(captor.capture());
        LinkRiskOutputData output = captor.getValue();

        assertNotNull(output);
        assertEquals(List.of(url1, url2), output.getUrls(), "URLs list mismatch");
        assertEquals(List.of("SAFE", "SAFE"), output.getRiskLevels(), "Risk levels list mismatch");
        verify(mockOutputBoundary, never()).prepareFailureView(anyString());
    }

    @Test
    void execute_urlsPresent_mixedRisk_shouldMapMixedLevels() throws Exception {
        String url1 = "http://good.com";
        String url2 = "http://bad.com/login";
        LinkRiskInputData input = new LinkRiskInputData("Check " + url1 + " or " + url2);

        when(mockDataAccess.checkUrls(List.of(url1, url2)))
                .thenReturn(Arrays.asList(
                        new LinkRiskResult(url1, "SAFE"),
                        new LinkRiskResult(url2, "DANGEROUS")
                ));

        interactor.execute(input);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<LinkRiskOutputData> captor = ArgumentCaptor.forClass(LinkRiskOutputData.class);
        verify(mockOutputBoundary, times(1)).prepareSuccessView(captor.capture());
        LinkRiskOutputData output = captor.getValue();

        assertNotNull(output);
        assertEquals(List.of(url1, url2), output.getUrls());
        assertEquals(List.of("SAFE", "DANGEROUS"), output.getRiskLevels());
        verify(mockOutputBoundary, never()).prepareFailureView(anyString());
    }

    @Test
    void execute_dataAccessThrows_shouldCallFailureView() throws Exception {
        String url = "http://some.url";
        LinkRiskInputData input = new LinkRiskInputData("Link: " + url);

        when(mockDataAccess.checkUrls(List.of(url)))
                .thenThrow(new LinkRiskApiException("API error"));

        interactor.execute(input);

        verify(mockOutputBoundary, times(1))
                .prepareFailureView("Unable to analyze links right now. Please try again later.");
        verify(mockOutputBoundary, never()).prepareSuccessView(any());
    }
}