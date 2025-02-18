package ludo.mentis.aciem.tabellarius.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
