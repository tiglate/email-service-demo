package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Account expired")
public class AccountExpiredException extends AuthenticationException {
    public AccountExpiredException(String msg) {
        super(msg);
    }
}