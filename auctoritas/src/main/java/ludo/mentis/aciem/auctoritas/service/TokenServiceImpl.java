package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${token.expiration:36000}") // Default 10 hours
    private long expirationSeconds;

    public TokenServiceImpl(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public String generateToken(String username, String[] roles) throws JOSEException {
        var signer = new RSASSASigner(privateKey);

        var claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", roles)
                .issuer("auctoritas-auth-server")
                .expirationTime(Date.from(Instant.now().plusSeconds(expirationSeconds)))
                .issueTime(new Date())
                .build();

        var signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.RS256),
                claimsSet
        );
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            var signedJWT = SignedJWT.parse(token);
            var verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

            if (!signedJWT.verify(verifier)) {
                return false;
            }

            var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && new Date().before(expirationTime);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
