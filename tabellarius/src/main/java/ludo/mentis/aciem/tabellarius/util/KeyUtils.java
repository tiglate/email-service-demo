package ludo.mentis.aciem.tabellarius.util;

import ludo.mentis.aciem.tabellarius.security.PublicKeyException;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {

    private static final String DUMMY_PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlJ3D1bOXDD/FhHjVPWHULVFmlo/pVvp7s"
          + "FkXHnF8CLWZqDx5ZcQLcVrcAnHApMsiuEzHnW1FZSPNP0ERt3vqMtHhbZrQXrt4+M0CsHqYGdzYB"
          + "KuqUSPuIMhSxmAubwVVuxyoF+OoQ7M8sep01Je82sy4X7avW6LHFXBXNlOsVqyARGQ2DlmbpquV+"
          + "wJ88m0k8m7/WY/IDxXYQbpXuUZG15YuU3T2F8A8y8KVsWBkBpgRFsmb+6CxZY0UivjjBaUKb1D/f"
          + "tbEi0Z4JAH1v8EB6iAcwzNvNKBl1XQLUklw5YLwotGvazBaD2Av/g+C52zHJwesz1pE0CP7C+HR8"
          + "6yyNwIDAQAB";

    private KeyUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static PublicKey getPublicKey(String publicKeyString) throws PublicKeyException {
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

    public static String getDummyPublicKey() {
        return DUMMY_PUBLIC_KEY;
    }
}