package use_case.save_email;

public interface SaveEmailOutputBoundary {
    void presentSuccess(SaveEmailOutputData outputData);
    void presentError(SaveEmailOutputData outputData);
}
