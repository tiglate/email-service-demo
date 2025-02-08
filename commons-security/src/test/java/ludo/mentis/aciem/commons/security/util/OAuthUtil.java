package ludo.mentis.aciem.commons.security.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.model.PublicKeyResponse;
import ludo.mentis.aciem.commons.security.model.TokenResponse;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class OAuthUtil {

    private static PrivateKey privateKey;

    private static RSAPublicKey publicKey;

    public OAuthUtil() throws NoSuchAlgorithmException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final var keyPair = keyPairGenerator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = (RSAPublicKey) keyPair.getPublic();
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PublicKeyResponse getValidPublicKeyResponse() {
        final var response = new PublicKeyResponse();
        response.setContent(Base64.encodeBase64String(publicKey.getEncoded()));
        return response;
    }

    public TokenResponse getValidTokenResponse() {
        final var response = new TokenResponse();
        try {
            var token = generateToken("username", 3600, List.of(new String[]{"ROLE_USER"}));
            response.setTokenType("Bearer");
            response.setAccessToken(token.serialize());
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return response;
    }

    public SignedJWT generateToken(String username, int expirationSeconds, Object roles) throws JOSEException {
        final var signer = new RSASSASigner(privateKey);
        final var claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", roles)
                .issuer("auctoritas-auth-server")
                .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .issueTime(new Date())
                .build();
        final var signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet
        );
        signedJWT.sign(signer);
        return signedJWT;
    }

    public static String getAnyPublicKey() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq/FxfiqzqDc+fF3x" +
                "DMjR1iparKkxVBbSiDoLViuM624MTBy3Y0AQJc67nwEvguhPfHGZPFfIvse" +
                "4f9S9CFztPKG4LcwhwvO71BtGpSUSiVU1rYA7BNnk7kIBrBPV25tknagZM4" +
                "o+AETOuWp0ub/bQ8YYX9Ii+Bq6/Bb8tnq52b7uJ90R4/vZkMec22nqb8qbU" +
                "P5V7Dv/EMHL82Z0Wq13kdPLD7lJnKmMEA6rAQmNLoRs8S0xPWTx1N6Hs/Vm" +
                "7G4TxmIfyq+JyDYbMv9Fas6LWPZ0cKRPnk3I9uzkKkDST985FHs5uCpGkfA" +
                "wOoIgTWQjiXirSnpD5dbuABqjtatJpQIDAQAB";
    }
}
