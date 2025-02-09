package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private AuthServiceImpl authService;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        MockitoAnnotations.openMocks(this);

        var keyPair = getKeyPair();
        publicKey = keyPair.getPublic();

        authService = new AuthServiceImpl(tokenService, publicKey, authenticationManager);
    }

    @Test
    void testGenerateToken_Success() throws JOSEException {
        var username = "testUser";
        var password = "testPassword";
        var token = "testToken";

        var authorities = Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
        var authentication = new DummyAuthentication(username, authorities);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(username, new String[]{"ROLE_USER"})).thenReturn(token);

        var result = authService.generateToken(username, password);

        assertNotNull(result);
        assertEquals("Bearer", result.get("token_type"));
        assertEquals(token, result.get("access_token"));
    }

    @Test
    void testGenerateToken_InvalidCredentials() {
        var username = "testUser";
        var password = "testPassword";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Invalid credentials") {
            private static final long serialVersionUID = -7765019233005440952L;
        });

        assertThrows(AuthenticationException.class, () -> authService.generateToken(username, password));
    }

    @Test
    void testValidateToken() {
        var token = "testToken";

        when(tokenService.validateToken(token)).thenReturn(true);

        var isValid = authService.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void testGetPublicKey() {
        var encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        var result = authService.getPublicKey();

        assertEquals(encodedPublicKey, result);
    }

    private static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}