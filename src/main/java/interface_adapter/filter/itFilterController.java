package interface_adapter.filter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import use_case.filter.FilterInputBoundary;
import use_case.filter.FilterInputData;
import view.ItDashboardView;

public class itFilterController {

    private ItDashboardView view;

    public itFilterController(ItDashboardView view) {
        this.view = view;

        view.getItFilterButton().addActionListener(e -> applyFilter());
        view.getItDiscordButton().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Pretend this opens the Discord server!"));
    }

    private void applyFilter() {
        // eventually call your real use case here

        DefaultTableModel model = (DefaultTableModel) view.getItEmailTable().getModel();
        model.setRowCount(0);  // clear table

        // mock email data (you can replace with real)
        Object[][] mockEmails = {
                {"Amazon", "Your account is locked", 92, "2025-11-10"},
                {"University", "Assignment posted", 4, "2025-11-11"},
                {"Bank", "Unusual login attempt", 88, "2025-11-06"}
        };

        for (Object[] row : mockEmails)
            model.addRow(row);
    }

}
