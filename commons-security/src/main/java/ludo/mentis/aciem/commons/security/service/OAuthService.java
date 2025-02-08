package ludo.mentis.aciem.commons.security.service;

import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.List;

/**
 * OAuth service interface for handling OAuth tokens.
 * <p>
 * This interface provides methods for obtaining OAuth tokens, extracting user details
 * and authorities from the token, and validating the token signature.
 * </p>
 */
public interface OAuthService {

    /**
     * Get the token from the OAuth service based on the username and password.
     *
     * @param username the username
     * @param password the password
     * @return the signed JWT
     * @throws ParseException            if an error occurs while parsing the token
     * @throws PublicKeyException        if an error occurs while getting the public key
     * @throws InvalidSignatureException if an error occurs while verifying the token signature
     */
    SignedJWT getToken(String username, String password) throws ParseException, PublicKeyException, InvalidSignatureException;

    /**
     * Gets the public key from the OAuth service.
     *
     * @return the public key
     * @throws PublicKeyException if an error occurs while getting the public key
     */
    PublicKey getPublicKey() throws PublicKeyException;

    /**
     * Parses the token and verifies its signature.
     *
     * @param token the token
     * @return the signed JWT
     * @throws ParseException            if an error occurs while parsing the token
     * @throws InvalidSignatureException if an error occurs while verifying the token signature
     * @throws PublicKeyException        if an error occurs while getting the public key
     */
    SignedJWT parseToken(String token) throws ParseException, InvalidSignatureException, PublicKeyException;

    /**
     * Extracts the authorities from the JWT payload.
     *
     * @param signedJWT the signed JWT
     * @return the authorities
     * @throws ParseException if an error occurs while extracting the authorities
     */
    List<SimpleGrantedAuthority> extractAuthorities(SignedJWT signedJWT) throws ParseException;


    /**
     * Extracts the username from the JWT payload.
     *
     * @param signedJWT the signed JWT
     * @return the username
     * @throws ParseException if an error occurs while extracting the username
     */
    String extractUsername(SignedJWT signedJWT) throws ParseException;

    /**
     * Validates the token.
     *
     * @param token the token
     * @return true if the token is valid, false otherwise
     */
    boolean validateToken(String token);
}
