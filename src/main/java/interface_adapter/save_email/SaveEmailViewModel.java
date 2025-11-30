package interface_adapter.save_email;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SaveEmailViewModel {
    private final PropertyChangeSupport support;
    private boolean success;
    private String message;

    public SaveEmailViewModel() {
        this.support = new PropertyChangeSupport(this);
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChange() {
        support.firePropertyChange("saveEmail", null, this);
    }
}
