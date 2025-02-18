package ludo.mentis.aciem.auctoritas.model;

import ludo.mentis.aciem.auctoritas.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 6087081873305564026L;
    private final Integer id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final OffsetDateTime accountExpirationDate;
    private final int failedLoginAttempts;
    private final OffsetDateTime lastFailedLoginAttempt;
    private final boolean accountLocked;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.accountExpirationDate = user.getAccountExpirationDate();
        this.failedLoginAttempts = user.getFailedLoginAttempts();
        this.lastFailedLoginAttempt = user.getLastFailedLoginAttempt();
        this.accountLocked = user.isAccountLocked();
        if (user.getRoles() == null) {
            this.authorities = Collections.emptyList();
        } else {
            this.authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getCode()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpirationDate == null || accountExpirationDate.isAfter(OffsetDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Integer getId() {
        return this.id;
    }

    public OffsetDateTime getAccountExpirationDate() {
        return this.accountExpirationDate;
    }

    public int getFailedLoginAttempts() {
        return this.failedLoginAttempts;
    }

    public OffsetDateTime getLastFailedLoginAttempt() {
        return this.lastFailedLoginAttempt;
    }

    public boolean isAccountLocked() {
        return this.accountLocked;
    }
}