package ludo.mentis.aciem.auctoritas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ludo.mentis.aciem.auctoritas.exception.AccountDisabledException;
import ludo.mentis.aciem.auctoritas.exception.AccountExpiredException;
import ludo.mentis.aciem.auctoritas.exception.AccountLockedException;
import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final var user = userService.findByUsername(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }

        if (userService.isAccountLocked(user)) {
            log.warn("user account locked: {}", username);
            throw new AccountLockedException(String.format("User %s is locked", username));
        }

        if (userService.isAccountExpired(user)) {
            log.warn("user account expired: {}", username);
            throw new AccountExpiredException(String.format("User %s is expired", username));
        }

        if (!userService.isAccountEnabled(user)) {
            log.warn("user account disabled: {}", username);
            throw new AccountDisabledException(String.format("User %s is disabled", username));
        }

        return new CustomUserDetails(user);
    }
}