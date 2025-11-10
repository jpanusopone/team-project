package use_case.filter;

/**
 * The Filter Interactor
 */
public class FilterInteractor implements FilterInputBoundary {
    private final FilterUserDataAccessInterface userDataAccessObject;
    private final FilterOutputBoundary filterPresenter;

    public FilterInteractor(FilterUserDataAccessInterface userDataAccessInterface,
                            FilterOutputBoundary filterOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.filterPresenter = filterOutputBoundary;
    }
    @Override
    public void execute(FilterInputData filterInputData) {
        //TODO implement execute
    }

}
