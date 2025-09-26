package ludo.mentis.aciem.auctoritas.listener;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.model.CustomUserDetails;
import ludo.mentis.aciem.auctoritas.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

import static org.mockito.Mockito.*;

class AuthenticationEventListenerTest {

    @Mock
    private UserService userService;

    private AuthenticationEventListener authenticationEventListener;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        authenticationEventListener = new AuthenticationEventListener(userService);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close(); // Release resources
    }

    @Test
    void testAuthenticationFailed_UserExists() {
        var username = "testUser";
        var user = new User();
        user.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(user);
        when(userService.increaseFailedAttempts(user)).thenReturn(3);

        var event = new AuthenticationFailureBadCredentialsEvent(
                new UsernamePasswordAuthenticationToken(username, "wrongPassword"),
                new AuthenticationException("Bad credentials") {
                    @Serial
                    private static final long serialVersionUID = 5684404282967299191L;
                });

        authenticationEventListener.authenticationFailed(event);

        verify(userService).findByUsername(username);
        verify(userService).increaseFailedAttempts(user);
    }

    @Test
    void testAuthenticationFailed_UserDoesNotExist() {
        var username = "testUser";

        when(userService.findByUsername(username)).thenReturn(null);

        var event = new AuthenticationFailureBadCredentialsEvent(
                new UsernamePasswordAuthenticationToken(username, "wrongPassword"),
                new AuthenticationException("Bad credentials") {
                    @Serial
                    private static final long serialVersionUID = 7147580949331772174L;
                });

        authenticationEventListener.authenticationFailed(event);

        verify(userService).findByUsername(username);
        verify(userService, never()).increaseFailedAttempts(any());
    }

    @Test
    void testAuthenticationSuccess_UserExists() {
        var username = "testUser";
        var user = new User();
        user.setUsername(username);
        var loggedUser = createCustomUserDetails(user);

        when(userService.findByUsername(username)).thenReturn(user);

        var event = new AuthenticationSuccessEvent(
                new UsernamePasswordAuthenticationToken(loggedUser, "password", loggedUser.getAuthorities()));

        authenticationEventListener.authenticationSuccess(event);

        verify(userService).findByUsername(username);
        verify(userService).resetFailedAttempts(user);
    }

    @Test
    void testAuthenticationSuccess_UserDoesNotExist() {
        var username = "testUser";
        var user = new User();
        user.setUsername(username);
        var loggedUser = createCustomUserDetails(user);

        when(userService.findByUsername(username)).thenReturn(null);

        var event = new AuthenticationSuccessEvent(
                new UsernamePasswordAuthenticationToken(loggedUser, "password", loggedUser.getAuthorities()));

        authenticationEventListener.authenticationSuccess(event);

        verify(userService).findByUsername(username);
        verify(userService, never()).resetFailedAttempts(any());
    }

    private CustomUserDetails createCustomUserDetails(User user) {
        return new CustomUserDetails(user);
    }
}