package use_case.filter;

import entity.Email;
import entity.EmailBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * Unit tests for FilterInteractor.
 */
public class FilterInteractorTest {

    @Test
    public void testSuccessfulFilterByScore() {
        // 1. Create mocks (fake versions of dependencies)
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // 2. Create test input data - filtering emails with suspicion score 50-100
        FilterInputData inputData = new FilterInputData(
                null,           // keyword
                null,           // sender
                null,           // sortBy
                50.0,           // minScore
                100.0           // maxScore
        );

        // 3. Create fake test emails using EmailBuilder
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2024, 1, 2, 11, 0);

        Email email1 = new EmailBuilder()
                .id(1)
                .title("Suspicious Email")
                .sender("scammer@fake.com")
                .body("Click here to win!")
                .dateReceived(date1)
                .suspicionScore(85.0)
                .verifiedStatus("unverified")
                .pinned(true)
                .build();

        Email email2 = new EmailBuilder()
                .id(2)
                .title("Another Phishing Attempt")
                .sender("phish@bad.com")
                .body("Your account needs verification")
                .dateReceived(date2)
                .suspicionScore(92.0)
                .verifiedStatus("verified")
                .pinned(true)
                .build();

        List<Email> testEmails = Arrays.asList(email1, email2);

        // 4. Tell the mock DAO to return our test emails when filter() is called
        when(mockDAO.filter(inputData)).thenReturn(testEmails);

        // 5. Create the real interactor (with fake dependencies) and run it
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // 6. Verify the presenter's SUCCESS method was called with correct data
        verify(mockPresenter).prepareSuccessView(argThat(outputData ->
                outputData.getTitles().equals(Arrays.asList("Suspicious Email", "Another Phishing Attempt")) &&
                        outputData.getSenders().equals(Arrays.asList("scammer@fake.com", "phish@bad.com")) &&
                        outputData.getDatesReceived().equals(Arrays.asList(date1.toString(), date2.toString())) &&
                        outputData.getSuspicionScores().equals(Arrays.asList("85.0", "92.0")) &&
                        outputData.getVerifiedStatuses().equals(Arrays.asList("unverified", "verified"))
        ));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    public void testInvalidScoreRange() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Invalid input: min score GREATER than max score
        FilterInputData invalidInput = new FilterInputData(
                null,
                null,
                null,
                100.0,  // minScore
                50.0    // maxScore - INVALID!
        );

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(invalidInput);

        // Verify the FAIL method was called with the right error message
        verify(mockPresenter).prepareFailView("Minimum score cannot be greater than maximum score.");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    public void testNullFilterInput() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Execute with null input
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(null);

        // Verify fail view was called with correct message
        verify(mockPresenter).prepareFailView("Filter cannot be null.");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    public void testDatabaseException() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Create valid input
        FilterInputData inputData = new FilterInputData(null, null, null, 50.0, 100.0);

        // Make the DAO throw an exception
        when(mockDAO.filter(inputData)).thenThrow(new RuntimeException("Database error"));

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // Verify fail view was called
        verify(mockPresenter).prepareFailView("Failed to load filtered emails.");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    public void testNoEmailsMatched() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Create valid input
        FilterInputData inputData = new FilterInputData(null, null, null, 50.0, 100.0);

        // Make DAO return empty list
        when(mockDAO.filter(inputData)).thenReturn(Arrays.asList());

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // Verify fail view was called
        verify(mockPresenter).prepareFailView("No emails matched.");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    public void testNullEmailsReturned() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Create valid input
        FilterInputData inputData = new FilterInputData(null, null, null, 50.0, 100.0);

        // Make DAO return null
        when(mockDAO.filter(inputData)).thenReturn(null);

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // Verify fail view was called
        verify(mockPresenter).prepareFailView("No emails matched.");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    public void testOnlyMinScoreProvided() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // Only minScore, no maxScore
        FilterInputData inputData = new FilterInputData(null, null, null, 50.0, null);

        // Create test emails
        Email email = new EmailBuilder()
                .id(1)
                .title("Test Email")
                .sender("test@test.com")
                .dateReceived(LocalDateTime.of(2024, 1, 1, 10, 0))
                .suspicionScore(75.0)
                .verifiedStatus("unverified")
                .build();

        when(mockDAO.filter(inputData)).thenReturn(Arrays.asList(email));

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // Should succeed (no validation error for missing maxScore)
        verify(mockPresenter).prepareSuccessView(any(FilterOutputData.class));
    }

    @Test
    public void testNoScoresProvided() {
        // Create mocks
        FilterUserDataAccessInterface mockDAO = mock(FilterUserDataAccessInterface.class);
        FilterOutputBoundary mockPresenter = mock(FilterOutputBoundary.class);

        // No scores at all
        FilterInputData inputData = new FilterInputData(null, null, null, null, null);

        // Create test emails
        Email email = new EmailBuilder()
                .id(1)
                .title("Test Email")
                .sender("test@test.com")
                .dateReceived(LocalDateTime.of(2024, 1, 1, 10, 0))
                .suspicionScore(75.0)
                .verifiedStatus("verified")
                .build();

        when(mockDAO.filter(inputData)).thenReturn(Arrays.asList(email));

        // Execute
        FilterInteractor interactor = new FilterInteractor(mockDAO, mockPresenter);
        interactor.execute(inputData);

        // Should succeed
        verify(mockPresenter).prepareSuccessView(any(FilterOutputData.class));
    }
}