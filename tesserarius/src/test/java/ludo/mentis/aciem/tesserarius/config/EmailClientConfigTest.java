package ludo.mentis.aciem.tesserarius.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmailClientConfigTest {

    @Mock
    private OAuthService oauthService;

    @Mock
    private SignedJWT signedJWT;

    private EmailClientConfig emailClientConfig;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        emailClientConfig = new EmailClientConfig(oauthService);
        emailClientConfig.username = "testUsername";
        emailClientConfig.password = "testPassword";
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close(); // Release resources
    }

    @Test
    void testGetBearerToken() throws ParseException, PublicKeyException, InvalidSignatureException {
        when(oauthService.getToken("testUsername", "testPassword")).thenReturn(signedJWT);
        when(signedJWT.serialize()).thenReturn("testToken");

        String token = emailClientConfig.getBearerToken();

        assertEquals("Bearer testToken", token);
        verify(oauthService, times(1)).getToken("testUsername", "testPassword");
    }

    @Test
    void testGetBearerToken_Exception() throws ParseException, PublicKeyException, InvalidSignatureException {
        when(oauthService.getToken("testUsername", "testPassword")).thenThrow(new ParseException("Error", 0));

        String token = emailClientConfig.getBearerToken();

        assertEquals("", token);
        verify(oauthService, times(1)).getToken("testUsername", "testPassword");
    }

    @Test
    void testRequestInterceptor() {
        Cache<String, String> tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
        emailClientConfig = new EmailClientConfig(oauthService);
        emailClientConfig.tokenCache = tokenCache;

        assertNotNull(emailClientConfig.requestInterceptor());
    }
}