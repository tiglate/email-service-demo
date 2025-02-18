package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Account expired")
public class AccountDisabledException extends AuthenticationException {

	private static final long serialVersionUID = -139374328614968917L;

	public AccountDisabledException(String msg) {
        super(msg);
    }
}