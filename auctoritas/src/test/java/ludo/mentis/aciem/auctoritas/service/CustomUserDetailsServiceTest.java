package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.exception.AccountDisabledException;
import ludo.mentis.aciem.auctoritas.exception.AccountExpiredException;
import ludo.mentis.aciem.auctoritas.exception.AccountLockedException;
import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customUserDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        var exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent");
        });

        assertEquals("User nonexistent not found", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_UserAccountLocked() {
        var user = new User();
        when(userService.findByUsername("lockedUser")).thenReturn(user);
        when(userService.isAccountLocked(user)).thenReturn(true);

        AccountLockedException exception = assertThrows(AccountLockedException.class, () -> {
            customUserDetailsService.loadUserByUsername("lockedUser");
        });

        assertEquals("User lockedUser is locked", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_UserAccountExpired() {
        var user = new User();
        when(userService.findByUsername("expiredUser")).thenReturn(user);
        when(userService.isAccountExpired(user)).thenReturn(true);

        var exception = assertThrows(AccountExpiredException.class, () -> {
            customUserDetailsService.loadUserByUsername("expiredUser");
        });

        assertEquals("User expiredUser is expired", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_UserAccountDisabled() {
        var user = new User();
        when(userService.findByUsername("disabledUser")).thenReturn(user);
        when(userService.isAccountEnabled(user)).thenReturn(false);

        var exception = assertThrows(AccountDisabledException.class, () -> {
            customUserDetailsService.loadUserByUsername("disabledUser");
        });

        assertEquals("User disabledUser is disabled", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_Success() {
        var user = new User();
        when(userService.findByUsername("validUser")).thenReturn(user);
        when(userService.isAccountLocked(user)).thenReturn(false);
        when(userService.isAccountExpired(user)).thenReturn(false);
        when(userService.isAccountEnabled(user)).thenReturn(true);

        var userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername("validUser");

        assertNotNull(userDetails);
    }
}