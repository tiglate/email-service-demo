package ludo.mentis.aciem.auctoritas.listener;

import lombok.extern.slf4j.Slf4j;
import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;
import ludo.mentis.aciem.auctoritas.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationEventListener {

    private final UserService userService;

    public AuthenticationEventListener(final UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        final var username = (String) event.getAuthentication().getPrincipal();
        final var user = userService.findByUsername(username);
        if (user != null) {
            int failedAttempts = userService.increaseFailedAttempts(user);
            if (failedAttempts >= 3) {
                log.warn("User account locked for username: {}", username);
            } else {
                log.warn("User \"{}\" failed login attempts: {}", user, failedAttempts);
            }
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