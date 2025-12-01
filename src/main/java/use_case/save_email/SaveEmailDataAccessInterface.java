package use_case.save_email;

import java.util.concurrent.ExecutionException;

import entity.Email;

/**
 * Data access interface for saving and checking email records.
 */
public interface SaveEmailDataAccessInterface {

    /**
     * Saves the given email entity.
     *
     * @param email the email object to save
     * @return the saved {@link Email} entity (possibly with updated fields)
     * @throws ExecutionException       if an asynchronous operation fails
     * @throws InterruptedException     if the operation is interrupted
     */
    Email saveEmail(Email email) throws ExecutionException, InterruptedException;

    /**
     * Checks whether an email with the same content already exists.
     *
     * @param body the email body to check for duplicates
     * @return {@code true} if an email with this body already exists; {@code false} otherwise
     * @throws ExecutionException       if an asynchronous operation fails
     * @throws InterruptedException     if the operation is interrupted
     */
    boolean emailExistsByContent(String body) throws ExecutionException, InterruptedException;
}
