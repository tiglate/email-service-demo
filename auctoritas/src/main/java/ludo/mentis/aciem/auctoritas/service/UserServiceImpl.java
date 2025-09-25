package ludo.mentis.aciem.auctoritas.service;

import java.time.OffsetDateTime;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import ludo.mentis.aciem.auctoritas.domain.User;
import ludo.mentis.aciem.auctoritas.repos.UserRepository;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000L; // 24 hours

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public int increaseFailedAttempts(User user) {
        final var newFailAttempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(newFailAttempts);
        user.setLastFailedLoginAttempt(OffsetDateTime.now());
        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLocked(true);
        }
        userRepository.save(user);
        return newFailAttempts;
    }

    @Override
    public void resetFailedAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setLastFailedLoginAttempt(null);
        userRepository.save(user);
    }

    @Override
    public boolean isAccountLocked(User user) {
        if (!user.isAccountLocked()) {
			return false;
		}
		if (user.getLastFailedLoginAttempt().plusSeconds(LOCK_TIME_DURATION / 1000).isAfter(OffsetDateTime.now())) {
			return true;
		}
		user.setAccountLocked(false);
		user.setFailedLoginAttempts(0);
		user.setLastFailedLoginAttempt(null);
		userRepository.save(user);
		return false;
    }

    @Override
    public boolean isAccountExpired(User user) {
        return user.getAccountExpirationDate() != null && user.getAccountExpirationDate().isBefore(OffsetDateTime.now());
    }

    @Override
    public boolean isAccountEnabled(User user) {
        return user.isEnabled();
    }

    @Override
    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public String getUsernameById(final int id) {
        return userRepository.getUsernameById(id);
    }
}
