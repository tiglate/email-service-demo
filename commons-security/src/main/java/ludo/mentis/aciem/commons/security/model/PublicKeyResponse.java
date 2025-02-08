package ludo.mentis.aciem.commons.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class PublicKeyResponse {

    @JsonProperty("public_key")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PublicKey getPublicKey() throws PublicKeyException {
        if (this.content == null || this.content.isEmpty()) {
            return null;
        }
        var publicKeyPEM = getPublicKeyPEM(content);
        try {
            var decoded = Base64.getDecoder().decode(publicKeyPEM);
            var keySpec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (IllegalArgumentException e) {
            throw new PublicKeyException("Invalid public key", e);
        } catch (Exception e) {
            throw new PublicKeyException("Failed to parse public key", e);
        }
    }

    private static String getPublicKeyPEM(String publicKeyString) {
        return publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicKeyResponse that = (PublicKeyResponse) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }
}
