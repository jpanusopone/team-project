package use_case.save_email;

import entity.Email;
import java.util.concurrent.ExecutionException;

public interface SaveEmailDataAccessInterface {
    Email saveEmail(Email email) throws ExecutionException, InterruptedException;
    boolean emailExistsByContent(String body) throws ExecutionException, InterruptedException;
}
