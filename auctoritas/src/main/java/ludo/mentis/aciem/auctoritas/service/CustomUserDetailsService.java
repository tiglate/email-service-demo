package ludo.mentis.aciem.auctoritas.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ludo.mentis.aciem.auctoritas.exception.AccountDisabledException;
import ludo.mentis.aciem.auctoritas.exception.AccountExpiredException;
import ludo.mentis.aciem.auctoritas.exception.AccountLockedException;
import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final var user = userService.findByUsername(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        if (userService.isAccountLocked(user)) {
            log.warn("user account locked: {}", username);
            throw new AccountLockedException("User " + username + " is locked");
        }

        if (userService.isAccountExpired(user)) {
            log.warn("user account expired: {}", username);
            throw new AccountExpiredException("User " + username + " is expired");
        }

        if (!userService.isAccountEnabled(user)) {
            log.warn("user account disabled: {}", username);
            throw new AccountDisabledException("User " + username + " is disabled");
        }

        return new CustomUserDetails(user);
    }
}