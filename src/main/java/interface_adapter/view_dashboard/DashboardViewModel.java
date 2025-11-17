package interface_adapter.view_dashboard;

import interface_adapter.ViewModel;

/**
 * The View Model for the Dashboard View.
 */
public class DashboardViewModel extends ViewModel<DashboardState>{
    public DashboardViewModel() {
        super("viewing dashboard");
        setState(new DashboardState());
    }
}
