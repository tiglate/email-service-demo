package ludo.mentis.aciem.auctoritas.service;

import ludo.mentis.aciem.auctoritas.domain.User;


public interface UserService {

    User findByUsername(String username);

    boolean isAccountLocked(User user);

    int increaseFailedAttempts(User user);

    void resetFailedAttempts(User user);

    boolean isAccountExpired(User user);

    boolean isAccountEnabled(User user);

    boolean usernameExists(final String username);

    String getUsernameById(final int id);
}
