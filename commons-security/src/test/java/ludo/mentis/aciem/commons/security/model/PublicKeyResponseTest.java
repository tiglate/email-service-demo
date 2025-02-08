package ludo.mentis.aciem.commons.security.model;

import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.util.OAuthUtil;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class PublicKeyResponseTest {

    @Test
    void testGettersAndSetters() {
        var publicKeyResponse = new PublicKeyResponse();
        publicKeyResponse.setContent("publicKeyContent");

        assertEquals("publicKeyContent", publicKeyResponse.getContent());
    }

    @Test
    void testGetPublicKey_Success() throws PublicKeyException {
        var publicKeyResponse = new PublicKeyResponse();
        publicKeyResponse.setContent(OAuthUtil.getAnyPublicKey());

        PublicKey publicKey = publicKeyResponse.getPublicKey();

        assertNotNull(publicKey);
    }

    @Test
    void testGetPublicKey_InvalidKey() {
        var publicKeyResponse = new PublicKeyResponse();
        publicKeyResponse.setContent("invalidKey");

        assertThrows(PublicKeyException.class, publicKeyResponse::getPublicKey);
    }

    @Test
    void testGetPublicKey_EmptyContent() throws PublicKeyException {
        var publicKeyResponse = new PublicKeyResponse();
        publicKeyResponse.setContent("");

        assertNull(publicKeyResponse.getPublicKey());
    }

    @Test
    void testEqualsAndHashCode() {
        var publicKeyResponse1 = new PublicKeyResponse();
        publicKeyResponse1.setContent("publicKeyContent");

        var publicKeyResponse2 = new PublicKeyResponse();
        publicKeyResponse2.setContent("publicKeyContent");

        assertEquals(publicKeyResponse1, publicKeyResponse2);
        assertEquals(publicKeyResponse1.hashCode(), publicKeyResponse2.hashCode());
    }

    @Test
    void testNotEquals() {
        var publicKeyResponse1 = new PublicKeyResponse();
        publicKeyResponse1.setContent("publicKeyContent1");

        var publicKeyResponse2 = new PublicKeyResponse();
        publicKeyResponse2.setContent("publicKeyContent2");

        assertNotEquals(publicKeyResponse1, publicKeyResponse2);
    }
}