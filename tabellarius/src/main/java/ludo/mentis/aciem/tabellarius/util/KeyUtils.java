package ludo.mentis.aciem.tabellarius.util;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class KeyUtils {

    private KeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static PublicKey getPublicKeyFromCertsEndpoint(String certsUrl) throws PublicKeyException {
        var restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(certsUrl, Map.class);

        if (response == null) {
            throw new PublicKeyException("Failed to fetch public key");
        }

        var publicKeyString = (String)response.get("public_key");

        if (publicKeyString == null || publicKeyString.isEmpty()) {
            throw new PublicKeyException("Failed to fetch public key");
        }

        try {
            var publicKeyPEM = getPublicKeyPEM(publicKeyString);
            var decoded = Base64.getDecoder().decode(publicKeyPEM);
            var keySpec = new X509EncodedKeySpec(decoded);
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new PublicKeyException("Failed to parse public key", e);
        }
    }

    private static String getPublicKeyPEM(String publicKeyString) {
        return publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
    }
}
