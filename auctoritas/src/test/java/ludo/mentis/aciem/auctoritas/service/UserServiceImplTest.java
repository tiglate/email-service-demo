package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close(); // Release resources
    }

    @Test
    void testFindByUsername() {
        var user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsernameIgnoreCase("testUser")).thenReturn(user);

        var result = userService.findByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
    }

    @Test
    void testIncreaseFailedAttempts() {
        var user = new User();
        user.setFailedLoginAttempts(1);

        var result = userService.increaseFailedAttempts(user);

        assertEquals(2, result);
        assertEquals(2, user.getFailedLoginAttempts());
        assertNotNull(user.getLastFailedLoginAttempt());
        verify(userRepository).save(user);
    }

    @Test
    void testIncreaseFailedAttempts_LockAccount() {
        var user = new User();
        user.setFailedLoginAttempts(2);

        var result = userService.increaseFailedAttempts(user);

        assertEquals(3, result);
        assertTrue(user.isAccountLocked());
        verify(userRepository).save(user);
    }

    @Test
    void testResetFailedAttempts() {
        var user = new User();
        user.setFailedLoginAttempts(2);
        user.setLastFailedLoginAttempt(OffsetDateTime.now());

        userService.resetFailedAttempts(user);

        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLastFailedLoginAttempt());
        verify(userRepository).save(user);
    }

    @Test
    void testIsAccountLocked_NotLocked() {
        var user = new User();
        user.setAccountLocked(false);

        var result = userService.isAccountLocked(user);

        assertFalse(result);
    }

    @Test
    void testIsAccountLocked_Locked() {
        var user = new User();
        user.setAccountLocked(true);
        user.setLastFailedLoginAttempt(OffsetDateTime.now().minusHours(1));

        var result = userService.isAccountLocked(user);

        assertTrue(result);
    }

    @Test
    void testIsAccountLocked_LockExpired() {
        var user = new User();
        user.setAccountLocked(true);
        user.setLastFailedLoginAttempt(OffsetDateTime.now().minusDays(2));

        var result = userService.isAccountLocked(user);

        assertFalse(result);
        assertFalse(user.isAccountLocked());
        assertEquals(0, user.getFailedLoginAttempts());
        assertNull(user.getLastFailedLoginAttempt());
        verify(userRepository).save(user);
    }

    @Test
    void testIsAccountExpired() {
        var user = new User();
        user.setAccountExpirationDate(OffsetDateTime.now().minusDays(1));

        var result = userService.isAccountExpired(user);

        assertTrue(result);
    }

    @Test
    void testIsAccountEnabled() {
        var user = new User();
        user.setEnabled(true);

        var result = userService.isAccountEnabled(user);

        assertTrue(result);
    }

    @Test
    void testUsernameExists() {
        when(userRepository.existsByUsernameIgnoreCase("testUser")).thenReturn(true);

        var result = userService.usernameExists("testUser");

        assertTrue(result);
    }

    @Test
    void testGetUsernameById() {
        when(userRepository.getUsernameById(1)).thenReturn("testUser");

        var result = userService.getUsernameById(1);

        assertEquals("testUser", result);
    }
}