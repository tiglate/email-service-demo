package ludo.mentis.aciem.tesserarius.exception;

import java.io.Serial;

public class PublicKeyException extends Exception {

    @Serial
    private static final long serialVersionUID = -3921942778386352056L;

    public PublicKeyException(String message) {
        super(message);
    }

    public PublicKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}