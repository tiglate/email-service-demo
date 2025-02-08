package ludo.mentis.aciem.commons.security.service;

import com.nimbusds.jose.JOSEException;
import feign.FeignException;
import ludo.mentis.aciem.commons.security.client.OAuthClient;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.model.PublicKeyResponse;
import ludo.mentis.aciem.commons.security.util.OAuthUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OAuthServiceImplTest {

    @Mock
    private OAuthClient oauthClient;

    @InjectMocks
    private OAuthServiceImpl oauthService;

    private static OAuthUtil oauthUtil;

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        oauthUtil = new OAuthUtil();
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetToken_Success() throws Exception {
        final var validToken = oauthUtil.getValidTokenResponse();
        final var validPublicKey = oauthUtil.getValidPublicKeyResponse();

        when(oauthClient.getToken(anyString(), anyString())).thenReturn(validToken);
        when(oauthClient.getPublicKey()).thenReturn(validPublicKey);

        final var result = oauthService.getToken("username", "password");
        assertNotNull(result);

        verify(oauthClient).getToken("username", "password");
    }

    @Test
    void testGetToken_NullToken() {
        final var validPublicKey = oauthUtil.getValidPublicKeyResponse();

        when(oauthClient.getToken(anyString(), anyString())).thenReturn(null);
        when(oauthClient.getPublicKey()).thenReturn(validPublicKey);

        assertThrows(AccessDeniedException.class, () -> oauthService.getToken("username", "password"));
    }

    @Test
    void testGetToken_InvalidCredentials() {
        final var request = feign.Request.create(feign.Request.HttpMethod.GET, "url", Map.of(), null, null, null);
        final var exception = new FeignException.Unauthorized("Unauthorized", request, null, null);

        when(oauthClient.getToken(anyString(), anyString())).thenThrow(exception);

        assertThrows(AccessDeniedException.class, () -> oauthService.getToken("username", "password"));
    }

    @Test
    void testGetToken_InternalServerError() {
        final var request = feign.Request.create(feign.Request.HttpMethod.GET, "url", Map.of(), null, null, null);
        final var exception = new FeignException.InternalServerError("Internal server error", request, null, null);

        when(oauthClient.getToken(anyString(), anyString())).thenThrow(exception);

        assertThrows(AuthenticationException.class, () -> oauthService.getToken("username", "password"));
    }

    @Test
    void testGetToken_OtherFeignException() {
        final var request = feign.Request.create(feign.Request.HttpMethod.GET, "url", Map.of(), null, null, null);
        final var exception = new FeignException.Forbidden("Forbidden", request, null, null);

        when(oauthClient.getToken(anyString(), anyString())).thenThrow(exception);

        assertThrows(AuthenticationException.class, () -> oauthService.getToken("username", "password"));
    }

    @Test
    void parseToken_Success() throws Exception {
        final var validToken = oauthUtil.getValidTokenResponse();
        final var validPublicKey = oauthUtil.getValidPublicKeyResponse();

        when(oauthClient.getPublicKey()).thenReturn(validPublicKey);

        final var result = oauthService.parseToken(validToken.getAccessToken());

        assertNotNull(result);
    }

    @Test
    void testParseToken_InvalidSignature() {
        final var validToken = oauthUtil.getValidTokenResponse();
        final var publicKeyResponse = new PublicKeyResponse();

        publicKeyResponse.setContent(oauthUtil.getAnyPublicKey());

        when(oauthClient.getPublicKey()).thenReturn(publicKeyResponse);

        assertThrows(InvalidSignatureException.class, () -> oauthService.parseToken(validToken.getAccessToken()));
    }

    @Test
    void getPublicKey_Success() throws Exception {
        final var validPublicKey = oauthUtil.getValidPublicKeyResponse();

        when(oauthClient.getPublicKey()).thenReturn(validPublicKey);

        final var result = oauthService.getPublicKey();

        assertNotNull(result);
    }

    @Test
    void getPublicKey_FeignException() {
        final var request = feign.Request.create(feign.Request.HttpMethod.GET, "url", Map.of(), null, null, null);
        final var exception = new FeignException.Forbidden("Forbidden", request, null, null);

        when(oauthClient.getPublicKey()).thenThrow(exception);

        assertThrows(PublicKeyException.class, () -> oauthService.getPublicKey());
    }

    @Test
    void getPublicKey_NullPublicKey() {
        when(oauthClient.getPublicKey()).thenReturn(null);

        assertThrows(PublicKeyException.class, () -> oauthService.getPublicKey());
    }

    @Test
    void testExtractUsername_Success() throws ParseException, JOSEException {
        final var username = "TestUser";
        final var validToken = oauthUtil.generateToken(username, 10, List.of(new String[]{"ROLE_USER"}));

        final var result = oauthService.extractUsername(validToken);

        assertEquals(username, result);
    }

    @Test
    void testExtractAuthorities_Success() throws Exception {
        final var roles = List.of(new String[]{"ROLE_TEST"});
        final var validToken = oauthUtil.generateToken("test", 10, roles);

        var result = oauthService.extractAuthorities(validToken);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(roles.get(0), result.get(0).getAuthority());
    }

    @Test
    void testExtractAuthorities_InvalidRolesClaim() throws Exception {
        final var validToken = oauthUtil.generateToken("test", 10, "ROLE_TEST");

        assertThrows(ParseException.class, () -> oauthService.extractAuthorities(validToken));
    }

    @Test
    void validateToken_Success() {
        final var validToken = oauthUtil.getValidTokenResponse();

        when(oauthClient.validateToken(anyString())).thenReturn(Map.of("valid", "true"));

        final var result = oauthService.validateToken(validToken.getAccessToken());

        assertTrue(result);
    }

    @Test
    void validateToken_InvalidToken() {
        final var invalidToken = "Invalid token";

        when(oauthClient.validateToken(anyString())).thenReturn(Map.of("valid", "false"));

        final var result = oauthService.validateToken(invalidToken);

        assertFalse(result);
    }

    @Test
    void validateToken_InvalidResponse() {
        final var validToken = oauthUtil.getValidTokenResponse();

        when(oauthClient.validateToken(anyString())).thenReturn(Map.of("other", "invalid"));

        final var result = oauthService.validateToken(validToken.getAccessToken());

        assertFalse(result);
    }

    @Test
    void validateToken_FeignException() {
        final var validToken = oauthUtil.getValidTokenResponse();
        final var request = feign.Request.create(feign.Request.HttpMethod.GET, "url", Map.of(), null, null, null);
        final var exception = new FeignException.Forbidden("Forbidden", request, null, null);

        when(oauthClient.validateToken(anyString())).thenThrow(exception);

        final var result = oauthService.validateToken(validToken.getAccessToken());

        assertFalse(result);
    }
}