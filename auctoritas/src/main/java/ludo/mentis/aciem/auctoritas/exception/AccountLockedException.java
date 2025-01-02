package ludo.mentis.aciem.auctoritas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED, reason = "Account locked")
public class AccountLockedException extends AuthenticationException {
    public AccountLockedException(String msg) {
        super(msg);
    }
}