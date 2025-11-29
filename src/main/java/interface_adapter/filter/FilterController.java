package interface_adapter.filter;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

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
    private final String discordInviteLinkURL = "https://discord.gg/FmME2xh7";

    public FilterController(DashboardView view, FilterInputBoundary filterInteractor) {
        this.view = view;
        this.filterInteractor = filterInteractor;

        // Connect button to filter action
        view.getFilterButton().addActionListener(e -> applyFilter());

        // Connect button to Discord Server Invitation
        view.getDiscordButton().addActionListener(e ->
//                JOptionPane.showMessageDialog(view, "Pretend this opens the Discord server!"));
        {
            // Opens invite link to join Discord server in browser
            try {
                Desktop.getDesktop().browse(new URI(discordInviteLinkURL));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.view,
                        "Unable to open browser.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
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

