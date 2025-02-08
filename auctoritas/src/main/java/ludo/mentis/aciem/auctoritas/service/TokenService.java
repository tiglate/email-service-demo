package ludo.mentis.aciem.auctoritas.service;

import org.springframework.security.core.AuthenticationException;

import com.nimbusds.jose.JOSEException;

public interface TokenService {
    String generateToken(String username, String[] roles) throws AuthenticationException, JOSEException;
    boolean validateToken(String token);
}