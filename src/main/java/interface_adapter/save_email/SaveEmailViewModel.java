package interface_adapter.save_email;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for the Save Email feature.
 * Stores the state related to the save result and notifies listeners when updated.
 */
public class SaveEmailViewModel {

    private final PropertyChangeSupport support;
    private boolean success;
    private String message;

    /**
     * Constructs an empty SaveEmailViewModel.
     */
    public SaveEmailViewModel() {
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Sets whether the save operation succeeded.
     *
     * @param success true if the email was saved successfully
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Sets the user-facing message to display.
     *
     * @param message status or error message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns whether the save operation succeeded.
     *
     * @return true if successful
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the message associated with the save operation.
     *
     * @return message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Adds a listener for save email state changes.
     *
     * @param listener a PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Fires a property change event to notify listeners.
     */
    public void firePropertyChange() {
        support.firePropertyChange("saveEmail", null, this);
    }
}
