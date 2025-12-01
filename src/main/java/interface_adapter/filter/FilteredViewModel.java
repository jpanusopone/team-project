package interface_adapter.filter;

import interface_adapter.ViewModel;

/**
 * The State information representing the filtered view.
 */
public class FilteredViewModel extends ViewModel<FilteredState> {

    public FilteredViewModel() {
        super("filtered");
        setState(new FilteredState());
    }
}
