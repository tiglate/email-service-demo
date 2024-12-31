package ludo.mentis.aciem.tabellarius.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Setter
@Getter
public class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    private Boolean rememberMe;

}
