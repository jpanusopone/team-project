package interface_adapter.filter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import use_case.filter.SortBy;
import view.DashboardView;

/**
 * Controller for the Filter User Case.
 */

public class FilterController {
    private final FilterInputBoundary userFilterUseCaseInteractor;

    public FilterController(FilterInputBoundary userFilterUseCaseInteractor) {
        this.userFilterUseCaseInteractor = userFilterUseCaseInteractor;

//        view.getFilterButton().addActionListener(e -> applyFilter());
//        view.getDiscordButton().addActionListener(e ->
//                JOptionPane.showMessageDialog(view, "Pretend this opens the Discord server!"));
    }

    // temporary method, replace with execute
//    private void applyFilter() {
//        // eventually call your real use case here
//
//        DefaultTableModel model = (DefaultTableModel) view.getEmailTable().getModel();
//        model.setRowCount(0);  // clear table
//
//        // mock email data (you can replace with real)
//        Object[][] mockEmails = {
//                {"Amazon", "Your account is locked", 92, "2025-11-10"},
//                {"University", "Assignment posted", 4, "2025-11-11"},
//                {"Bank", "Unusual login attempt", 88, "2025-11-06"}
//        };
//
//        for (Object[] row : mockEmails)
//            model.addRow(row);

    /**
     * Executes the Filter Use Case
     * @param keyword the keyword the user wishes to filter by
     * @param sender the sender the user wishes to filter by
     * @param sortBy sorts by title, sender, date received, or suspicion score
     * @param minSuspicion the minimum suspicion the user filters by
     * @param maxSuspicion the maximum suspicion the user filters by
     */
    public void execute(String keyword, String sender, SortBy sortBy,
                        Double minSuspicion, Double maxSuspicion) {
        FilterInputData inputData = new FilterInputData(
                keyword,
                sender,
                sortBy,
                minSuspicion,
                maxSuspicion
        );

        userFilterUseCaseInteractor.execute(inputData);
    }
}

