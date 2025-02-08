package ludo.mentis.aciem.commons.security;

import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomAuthenticationProviderTest {

    @Mock
    private OAuthService oauthService;

    @InjectMocks
    private CustomAuthenticationProvider customAuthenticationProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_Success() throws Exception {
        var username = "testUser";
        var password = "testPassword";
        var signedJWT = mock(SignedJWT.class);
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        when(oauthService.getToken(username, password)).thenReturn(signedJWT);
        when(oauthService.extractAuthorities(signedJWT)).thenReturn(authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        var result = customAuthenticationProvider.authenticate(authentication);

        assertNotNull(result);
        assertEquals(username, ((User) result.getPrincipal()).getUsername());
        assertEquals(password, result.getCredentials());
        assertEquals(authorities, result.getAuthorities());
    }

    @Test
    void authenticate_InvalidCredentials() throws Exception {
        var username = "testUser";
        var password = "testPassword";

        when(oauthService.getToken(username, password)).thenThrow(new AccessDeniedException("Invalid credentials"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        assertThrows(AuthenticationException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    void authenticate_ParseException() throws Exception {
        var username = "testUser";
        var password = "testPassword";

        when(oauthService.getToken(username, password)).thenThrow(new ParseException("Parse error", 0));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        assertThrows(AuthenticationException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    void authenticate_PublicKeyException() throws Exception {
        var username = "testUser";
        var password = "testPassword";

        when(oauthService.getToken(username, password)).thenThrow(new PublicKeyException("Public key error"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        assertThrows(AuthenticationException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    void authenticate_InvalidSignatureException() throws Exception {
        var username = "testUser";
        var password = "testPassword";

        when(oauthService.getToken(username, password)).thenThrow(new InvalidSignatureException("Invalid signature"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        assertThrows(AuthenticationException.class, () -> customAuthenticationProvider.authenticate(authentication));
    }

    @Test
    void supports_UsernamePasswordAuthenticationToken() {
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void supports_OtherAuthenticationToken() {
        assertFalse(customAuthenticationProvider.supports(Authentication.class));
    }
}