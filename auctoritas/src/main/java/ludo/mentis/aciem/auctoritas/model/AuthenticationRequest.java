package ludo.mentis.aciem.auctoritas.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 72)
    private String password;

    @NotNull
    private Boolean rememberMe;

    public @NotNull @Size(max = 255) String getUsername() {
        return this.username;
    }

    public @NotNull @Size(max = 72) String getPassword() {
        return this.password;
    }

    public @NotNull Boolean getRememberMe() {
        return this.rememberMe;
    }

    public void setUsername(@NotNull @Size(max = 255) String username) {
        this.username = username;
    }

    public void setPassword(@NotNull @Size(max = 72) String password) {
        this.password = password;
    }

    public void setRememberMe(@NotNull Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
