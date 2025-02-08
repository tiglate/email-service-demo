package ludo.mentis.aciem.commons.security.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenResponseTest {

    @Test
    void testGettersAndSetters() {
        var tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("accessToken");
        tokenResponse.setTokenType("tokenType");

        assertEquals("accessToken", tokenResponse.getAccessToken());
        assertEquals("tokenType", tokenResponse.getTokenType());
    }

    @Test
    void testEqualsAndHashCode() {
        var tokenResponse1 = new TokenResponse();
        tokenResponse1.setAccessToken("accessToken");
        tokenResponse1.setTokenType("tokenType");

        var tokenResponse2 = new TokenResponse();
        tokenResponse2.setAccessToken("accessToken");
        tokenResponse2.setTokenType("tokenType");

        assertEquals(tokenResponse1, tokenResponse2);
        assertEquals(tokenResponse1.hashCode(), tokenResponse2.hashCode());
    }

    @Test
    void testNotEquals() {
        var tokenResponse1 = new TokenResponse();
        tokenResponse1.setAccessToken("accessToken1");
        tokenResponse1.setTokenType("tokenType1");

        var tokenResponse2 = new TokenResponse();
        tokenResponse2.setAccessToken("accessToken2");
        tokenResponse2.setTokenType("tokenType2");

        assertNotEquals(tokenResponse1, tokenResponse2);
    }
}