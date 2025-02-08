package ludo.mentis.aciem.commons.security;

import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import java.text.ParseException;


/**
 * Custom authentication provider for handling authentication using OAuth tokens.
 * <p>
 * This class implements the {@link AuthenticationProvider} interface and provides
 * a custom authentication mechanism using an {@link OAuthService} to validate
 * user credentials and extract authorities from a signed JWT token.
 * </p>
 * <p>
 * The authentication process involves:
 * <ul>
 *   <li>Retrieving the username and password from the {@link Authentication} object.</li>
 *   <li>Using the {@link OAuthService} to obtain a signed JWT token.</li>
 *   <li>Extracting user authorities from the signed JWT token.</li>
 *   <li>Creating a {@link UsernamePasswordAuthenticationToken} with the user details and authorities.</li>
 * </ul>
 * </p>
 * <p>
 * If authentication fails due to parsing errors, invalid signatures, or access denial,
 * an {@link AuthenticationException} is thrown with an appropriate error message.
 * </p>
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final OAuthService oauthService;

    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

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
            log.error("Error to authenticate user: {}. Message: {}", username, e.getMessage());
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