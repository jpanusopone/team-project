// ExplanationException.java
package use_case.interfaces;

public class ExplanationException extends Exception {
    public ExplanationException(String message) {
        super(message);
    }

    public ExplanationException(String message, Throwable cause) {
        super(message, cause);
    }
}