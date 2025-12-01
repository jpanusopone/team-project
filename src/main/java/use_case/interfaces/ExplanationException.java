// ExplanationException.java

package use_case.interfaces;

/**
 * Exception thrown when email explanation generation fails.
 */
public class ExplanationException extends Exception {

    /**
     * Creates an ExplanationException with a message.
     *
     * @param message the detail message
     */
    public ExplanationException(String message) {
        super(message);
    }

    /**
     * Creates an ExplanationException with a message and cause.
     *
     * @param message the detail message
     * @param cause   the underlying cause
     */
    public ExplanationException(String message, Throwable cause) {
        super(message, cause);
    }
}
