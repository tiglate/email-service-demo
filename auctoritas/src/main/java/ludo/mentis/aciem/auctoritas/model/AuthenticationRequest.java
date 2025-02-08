package ludo.mentis.aciem.auctoritas.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationRequest {

    @NotNull
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(max = 72)
    private String password;

    @NotNull
    private Boolean rememberMe;

}
