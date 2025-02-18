package ludo.mentis.aciem.auctoritas.service;

import java.util.Map;

import org.springframework.security.core.AuthenticationException;

import com.nimbusds.jose.JOSEException;

public interface AuthService {
    Map<String, String> generateToken(String username, String password) throws AuthenticationException, JOSEException;
    boolean validateToken(String token);
    String getPublicKey();
}