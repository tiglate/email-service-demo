package ludo.mentis.aciem.commons.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PublicKeyException extends Exception {

    private static final long serialVersionUID = -3921942778386352056L;

    public PublicKeyException(String message) {
        super(message);
    }

    public PublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
