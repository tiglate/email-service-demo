package ludo.mentis.aciem.auctoritas.service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenServiceImpl implements TokenService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${token.expiration:36000}") // Default 10 hours
    long expirationSeconds;

    private final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    public TokenServiceImpl(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    @Override
    public String generateToken(String username, String[] roles) throws JOSEException {
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

        return signedJWT.serialize();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            final var signedJWT = SignedJWT.parse(token);
            final var verifier = new RSASSAVerifier((RSAPublicKey) publicKey);

            if (!signedJWT.verify(verifier)) {
                log.error("Token verification failed");
                return false;
            }

            final var expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            return expirationTime != null && new Date().before(expirationTime);
        } catch (ParseException | JOSEException e) {
            log.error("Error validating token", e);
            return false;
        }
    }
}
