package interface_adapter.explain_phishing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ExplainPhishing ViewModel.
 *
 * <p>Responsibility: Hold the state and notify observers of state changes.
 *
 * <p>Clean Architecture Layer: Interface Adapter Layer
 *
 * <p>Pattern: Observer Pattern (PropertyChangeSupport)
 */
public class ExplainPhishingViewModel {
    private static final String VIEW_NAME = "explainPhishing";

    private final PropertyChangeSupport support;
    private ExplainPhishingState state;

    public ExplainPhishingViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.state = new ExplainPhishingState();
    }

    public String getViewName() {
        return VIEW_NAME;
    }

    public ExplainPhishingState getState() {
        return state;
    }

    /**
     * Sets the current state and notifies listeners of the change.
     *
     * @param state the new ExplainPhishing state
     */
    public void setState(ExplainPhishingState state) {
        final ExplainPhishingState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

    /**
     * Adds a listener to be notified when the state changes.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a previously added state-change listener.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Notifies listeners that the state has changed.
     */
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }
}
