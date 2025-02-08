package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Account expired")
public class AccountExpiredException extends AuthenticationException {

	private static final long serialVersionUID = -7710306274382968351L;

	public AccountExpiredException(String msg) {
        super(msg);
    }
}