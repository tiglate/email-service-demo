package ludo.mentis.aciem.auctoritas.model;

import java.time.OffsetDateTime;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
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
}
