package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -239312915132206265L;

	public NotFoundException() {
        super();
    }

    public NotFoundException(final String message) {
        super(message);
    }

}
