// use_case/it_dashboard_status/ItUpdateStatusOutputBoundary.java
package use_case.it_dashboard_status;

public interface ItUpdateStatusOutputBoundary {
    void prepareSuccessView(ItUpdateStatusOutputData data);
    void prepareFailView(String errorMessage);
}
