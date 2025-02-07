package ludo.mentis.aciem.commons.security.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
import ludo.mentis.aciem.commons.security.client.OAuthClient;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.model.PublicKeyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OAuthServiceImpl implements OAuthService {

    private final OAuthClient oauthClient;

    public OAuthServiceImpl(OAuthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    @Override
    public SignedJWT getToken(String username, String password) throws ParseException, PublicKeyException, InvalidSignatureException {
        String tokenString;
        try {
            var token = oauthClient.getToken(username, password);
            if (token == null) {
                throw new AccessDeniedException("The token returned by the OAuth service is null");
            }
            tokenString = token.getAccessToken();
        } catch (FeignException e) {
            if (e.status() == HttpStatus.UNAUTHORIZED.value()) {
                throw new AccessDeniedException("Invalid credentials", e);
            } else if (e.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                throw new AuthenticationException("Internal server error", e) {
                    private static final long serialVersionUID = 1L;
                };
            } else {
                throw new AuthenticationException("Authentication failed. Status Code: " + e.status(), e) {
                    private static final long serialVersionUID = 1L;
                };
            }
        } catch (RestClientException e) {
            throw new AuthenticationException("Authentication failed", e) {
                private static final long serialVersionUID = 1L;
            };
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
            throw new InvalidSignatureException(e.getMessage(), e);
        }
        if (!isValid) {
            throw new InvalidSignatureException("Invalid token signature");
        }
        return signedJWT;
    }

    @Override
    public PublicKey getPublicKey() throws PublicKeyException {
        PublicKeyResponse publicKeyResponse;

        try {
            publicKeyResponse = oauthClient.getPublicKey();
        } catch (RestClientException e) {
            throw new PublicKeyException("Failed to get public key", e);
        }

        if (publicKeyResponse == null) {
            throw new PublicKeyException("Public key is null");
        }

        return publicKeyResponse.getPublicKey();
    }

    @Override
    public List<SimpleGrantedAuthority> extractAuthorities(SignedJWT signedJWT) throws ParseException {
        var claims = signedJWT.getJWTClaimsSet();
        var rolesClaim = claims.getClaim("roles");
        if (!(rolesClaim instanceof List<?>)) {
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
            return validationResponse.get("valid").equals("true");
        } catch (RestClientException e) {
            return false;
        }
    }
}
