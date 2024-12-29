package ludo.mentis.aciem.tabellarius.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PublicKeyException extends Exception {

    public PublicKeyException(String message) {
        super(message);
    }

    public PublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
