package view;

import interface_adapter.filter.FilterController;
import interface_adapter.view_dashboard.DashboardViewModel;
import interface_adapter.view_dashboard.GetPinnedEmailsController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user selects to go to the dashboard.
 */

public class DashboardView extends JPanel implements PropertyChangeListener {

    private final String viewName = "dashboard";
    private final DashboardViewModel dashboardViewModel;
    private final FilterController filterController = null;
    private final GetPinnedEmailsController getPinnedEmailsController = null;
    // add your use case controllers here

    // add swing components here later

    public DashboardView(DashboardViewModel dashboardViewModel,
                         FilterController filterController,
                         GetPinnedEmailsController getPinnedEmailsController) {
        this.dashboardViewModel = dashboardViewModel;
        this.dashboardViewModel.addPropertyChangeListener(this);

        // define swing components
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
