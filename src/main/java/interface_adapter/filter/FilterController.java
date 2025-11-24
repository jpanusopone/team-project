package interface_adapter.filter;

import javax.swing.*;
import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;
import view.DashboardView;

/**
 * Controller for the Filter Use Case.
 */
public class FilterController {
    private final DashboardView view;
    private final FilterInputBoundary filterInteractor;

    public FilterController(DashboardView view, FilterInputBoundary filterInteractor) {
        this.view = view;
        this.filterInteractor = filterInteractor;

        // Connect button to filter action
        view.getFilterButton().addActionListener(e -> applyFilter());
        view.getDiscordButton().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Pretend this opens the Discord server!"));
    }

    /**
     * Apply filter based on user inputs from the dashboard
     */
    private void applyFilter() {
        // Get inputs from view
        String keyword = view.getKeyword();
        String sender = view.getSender();
        String sortSelection = view.getSort();

        // Convert sort string to SortBy enum
        SortBy sortBy = convertToSortBy(sortSelection);

        // Create input data
        FilterInputData inputData = new FilterInputData(
                keyword.isEmpty() ? null : keyword,
                sender.isEmpty() ? null : sender,
                sortBy,
                null,  // minScore - could add input fields for these later
                null   // maxScore
        );

        // Execute the filter use case
        filterInteractor.execute(inputData);
    }

    /**
     * Convert dropdown selection to SortBy enum
     */
    private SortBy convertToSortBy(String selection) {
        switch (selection) {
            case "Sender":
                return SortBy.SENDER;
            case "Date":
                return SortBy.DATE_RECEIVED;
            case "Suspicion Score":
                return SortBy.SUSPICION_SCORE;
            default:
                return SortBy.DATE_RECEIVED;
        }
    }
}

