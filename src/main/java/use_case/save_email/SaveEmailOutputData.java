package use_case.save_email;

public class SaveEmailOutputData {
    private final boolean success;
    private final String message;

    public SaveEmailOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
