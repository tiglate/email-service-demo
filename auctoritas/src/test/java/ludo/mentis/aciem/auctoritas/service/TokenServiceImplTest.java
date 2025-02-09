package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    private PublicKey publicKey;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        MockitoAnnotations.openMocks(this);

        final var keyPair = getKeyPair();

        publicKey = keyPair.getPublic();

        tokenService = new TokenServiceImpl(keyPair.getPrivate(), publicKey);
        tokenService.expirationSeconds = 36000; // 10 hours
    }

    @Test
    void testGenerateToken() throws JOSEException, ParseException {
        var username = "testUser";
        var roles = new String[]{"ROLE_USER"};

        var token = tokenService.generateToken(username, roles);

        assertNotNull(token);

        var signedJWT = SignedJWT.parse(token);
        assertEquals(username, signedJWT.getJWTClaimsSet().getSubject());
        assertArrayEquals(roles, signedJWT.getJWTClaimsSet().getStringArrayClaim("roles"));
        assertEquals("auctoritas-auth-server", signedJWT.getJWTClaimsSet().getIssuer());
        assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
    }

    @Test
    void testValidateToken_ValidToken() throws JOSEException {
        var token = tokenService.generateToken("testUser", new String[]{"ROLE_USER"});

        assertTrue(tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidSignature() throws JOSEException, NoSuchAlgorithmException {
        var keyPair = getKeyPair();
        var tokenService2 = new TokenServiceImpl(keyPair.getPrivate(), keyPair.getPublic());

        var token = tokenService2.generateToken("testUser", new String[]{"ROLE_USER"});

        assertFalse(tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken() throws JOSEException {
        tokenService.expirationSeconds = -1; // Set expiration to past

        var token = tokenService.generateToken("testUser", new String[]{"ROLE_USER"});

        assertFalse(tokenService.validateToken(token));
    }

    @Test
    void testValidateToken_ParseException() {
        var token = "invalidToken";

        assertFalse(tokenService.validateToken(token));
    }

    private static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}