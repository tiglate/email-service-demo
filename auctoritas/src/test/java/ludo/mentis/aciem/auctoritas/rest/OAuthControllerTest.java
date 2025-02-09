package ludo.mentis.aciem.auctoritas.rest;

import com.nimbusds.jose.JOSEException;
import ludo.mentis.aciem.auctoritas.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OAuthControllerTest {

    @Mock
    private AuthService authService;

    private OAuthController oAuthController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        oAuthController = new OAuthController(authService);
    }

    @Test
    void testGenerateToken_Success() throws JOSEException {
        var username = "testUser";
        var password = "testPassword";
        var tokenResponse = Map.of("token_type", "Bearer", "access_token", "testToken");

        when(authService.generateToken(username, password)).thenReturn(tokenResponse);

        ResponseEntity<Map<String, String>> response = oAuthController.generateToken(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tokenResponse, response.getBody());
    }

    @Test
    void testGenerateToken_InvalidCredentials() throws JOSEException {
        var username = "testUser";
        var password = "wrongPassword";

        when(authService.generateToken(username, password)).thenThrow(new AuthenticationException("Invalid credentials") {});

        ResponseEntity<Map<String, String>> response = oAuthController.generateToken(username, password);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("error", "Invalid credentials"), response.getBody());
    }

    @Test
    void testGenerateToken_InternalServerError() throws JOSEException {
        var username = "testUser";
        var password = "testPassword";

        when(authService.generateToken(username, password)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<Map<String, String>> response = oAuthController.generateToken(username, password);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(Map.of("error", "Internal server error"), response.getBody());
    }

    @Test
    void testGetPublicKey() {
        var publicKeyString = "publicKey";

        when(authService.getPublicKey()).thenReturn(publicKeyString);

        ResponseEntity<Map<String, String>> response = oAuthController.getPublicKey();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("public_key", publicKeyString), response.getBody());
    }

    @Test
    void testValidateToken_Valid() {
        var token = "validToken";

        when(authService.validateToken(token)).thenReturn(true);

        ResponseEntity<Map<String, String>> response = oAuthController.validateToken(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Map.of("valid", "true"), response.getBody());
    }

    @Test
    void testValidateToken_Invalid() {
        var token = "invalidToken";

        when(authService.validateToken(token)).thenReturn(false);

        ResponseEntity<Map<String, String>> response = oAuthController.validateToken(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(Map.of("valid", "false"), response.getBody());
    }
}