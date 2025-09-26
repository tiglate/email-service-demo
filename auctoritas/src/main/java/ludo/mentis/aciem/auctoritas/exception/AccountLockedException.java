package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Account locked")
public class AccountLockedException extends AuthenticationException {

    @Serial
	private static final long serialVersionUID = 1444980166898628531L;

	public AccountLockedException(String msg) {
        super(msg);
    }
}