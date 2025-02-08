package ludo.mentis.aciem.auctoritas.listener;

import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;
import ludo.mentis.aciem.auctoritas.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEventListener {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationEventListener.class);
    private final UserService userService;

    public AuthenticationEventListener(final UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        final var username = (String) event.getAuthentication().getPrincipal();
        final var user = userService.findByUsername(username);
        if (user == null) {
            return;
        }
        final int failedAttempts = userService.increaseFailedAttempts(user);
        if (failedAttempts >= 3) {
            log.warn("User account locked for username: {}", username);
        } else {
            log.warn("User \"{}\" failed login attempts: {}", user, failedAttempts);
        }
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        final var loggedUser = (CustomUserDetails) event.getAuthentication().getPrincipal();
        final var user = userService.findByUsername(loggedUser.getUsername());
        if (user != null) {
            userService.resetFailedAttempts(user);
        }
    }
}