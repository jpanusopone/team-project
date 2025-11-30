package interface_adapter.explain_phishing;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ExplainPhishing ViewModel.
 *
 * Responsibility: Hold the state and notify observers of state changes.
 *
 * Clean Architecture Layer: Interface Adapter Layer
 * Pattern: Observer Pattern (PropertyChangeSupport)
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

    public void setState(ExplainPhishingState state) {
        ExplainPhishingState oldState = this.state;
        this.state = state;
        support.firePropertyChange("state", oldState, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }
}
