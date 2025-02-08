package ludo.mentis.aciem.commons.security.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import ludo.mentis.aciem.commons.security.client.OAuthClient;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.model.PublicKeyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OAuthServiceImpl implements OAuthService {

    private final OAuthClient oauthClient;
    private final Logger log = LoggerFactory.getLogger(OAuthServiceImpl.class);

    public OAuthServiceImpl(OAuthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    @Override
    public SignedJWT getToken(String username, String password) throws ParseException, PublicKeyException, InvalidSignatureException {
        String tokenString;
        try {
            var token = oauthClient.getToken(username, password);
            if (token == null) {
                log.error("The token returned by the OAuth service is null.");
                throw new AccessDeniedException("The token returned by the OAuth service is null.");
            }
            tokenString = token.getAccessToken();
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                log.error("Error to get a token. Http Status: UNAUTHORIZED.");
                throw new AccessDeniedException("Invalid credentials.", e);
            } else if (e.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("Error to get a token. Http Status: INTERNAL_SERVER_ERROR.");
                throw new AuthenticationException("Internal server error.", e) {
                    private static final long serialVersionUID = 1L;
                };
            } else {
                log.error("Error to get a token. Status code: {}", e.status());
                throw new AuthenticationException("Authentication failed. Status Code: " + e.status(), e) {
                    private static final long serialVersionUID = 1L;
                };
            }
        }
        return parseToken(tokenString);
    }

    @Override
    public SignedJWT parseToken(String token) throws ParseException, InvalidSignatureException, PublicKeyException {
        var publicKey = (RSAPublicKey) getPublicKey();
        var signedJWT = SignedJWT.parse(token);
        var verifier = new RSASSAVerifier(publicKey);
        var isValid = false;
        try {
            isValid = signedJWT.verify(verifier);
        } catch (JOSEException e) {
            log.error("Error to verify the token signature. Message: {}", e.getMessage());
            throw new InvalidSignatureException(e.getMessage(), e);
        }
        if (!isValid) {
            log.error("Invalid token signature.");
            throw new InvalidSignatureException("Invalid token signature");
        }
        return signedJWT;
    }

    @Override
    public PublicKey getPublicKey() throws PublicKeyException {
        PublicKeyResponse publicKeyResponse;

        try {
            publicKeyResponse = oauthClient.getPublicKey();
        } catch (FeignException e) {
            log.error("Error to get public key. Status code: {}. Message: {}", e.status(), e.getMessage());
            throw new PublicKeyException("Failed to get public key", e);
        }

        if (publicKeyResponse == null) {
            log.error("Public key is null.");
            throw new PublicKeyException("Public key is null");
        }

        return publicKeyResponse.getPublicKey();
    }

    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(SignedJWT signedJWT) throws ParseException {
        var claims = signedJWT.getJWTClaimsSet();
        var rolesClaim = claims.getClaim("roles");
        if (!(rolesClaim instanceof List<?>)) {
            log.error("Invalid roles claim. The claim is not a list.");
            throw new ParseException("Invalid roles claim", 0);
        }
        var roles = (List<?>) rolesClaim;
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String extractUsername(SignedJWT signedJWT) throws ParseException {
        var claims = signedJWT.getJWTClaimsSet();
        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            var validationResponse = oauthClient.validateToken(token);
            return validationResponse.containsKey("valid") &&
                    validationResponse.get("valid").equals("true");
        } catch (FeignException e) {
            log.error("Error to validate the token. Status code: {}. Message: {}", e.status(), e.getMessage());
            return false;
        }
    }
}
