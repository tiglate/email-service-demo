package ludo.mentis.aciem.commons.security;

import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import java.text.ParseException;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final OAuthService oauthService;

    public CustomAuthenticationProvider(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        try {
            var signedJWT = oauthService.getToken(username, password);
            var authorities = oauthService.extractAuthorities(signedJWT);
            var userDetails = new User(username, password, authorities);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (ParseException | PublicKeyException | InvalidSignatureException | AccessDeniedException e) {
            throw new AuthenticationException("Authentication failed", e) {
                private static final long serialVersionUID = -8550290991491789722L;
            };
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}