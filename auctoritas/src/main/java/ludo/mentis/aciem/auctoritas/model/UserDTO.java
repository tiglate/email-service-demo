package ludo.mentis.aciem.auctoritas.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;


public class UserDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    @UserUsernameUnique
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    private boolean enabled;

    private boolean accountLocked;

    private int failedLoginAttempts;

    private OffsetDateTime lastFailedLoginAttempt;

    private OffsetDateTime accountExpirationDate;

    private List<Integer> roles;

    private Integer software;

    @AssertTrue(message = "Account expiration date must be null or in the future")
    private boolean isAccountExpirationDateValid() {
        return accountExpirationDate == null || accountExpirationDate.isAfter(OffsetDateTime.now());
    }

    public Integer getId() {
        return this.id;
    }

    public @NotNull @Size(max = 255) String getUsername() {
        return this.username;
    }

    public @NotNull @Size(max = 255) String getPassword() {
        return this.password;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAccountLocked() {
        return this.accountLocked;
    }

    public int getFailedLoginAttempts() {
        return this.failedLoginAttempts;
    }

    public OffsetDateTime getLastFailedLoginAttempt() {
        return this.lastFailedLoginAttempt;
    }

    public OffsetDateTime getAccountExpirationDate() {
        return this.accountExpirationDate;
    }

    public List<Integer> getRoles() {
        return this.roles;
    }

    public Integer getSoftware() {
        return this.software;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(@NotNull @Size(max = 255) String username) {
        this.username = username;
    }

    public void setPassword(@NotNull @Size(max = 255) String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public void setLastFailedLoginAttempt(OffsetDateTime lastFailedLoginAttempt) {
        this.lastFailedLoginAttempt = lastFailedLoginAttempt;
    }

    public void setAccountExpirationDate(OffsetDateTime accountExpirationDate) {
        this.accountExpirationDate = accountExpirationDate;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public void setSoftware(Integer software) {
        this.software = software;
    }
}
