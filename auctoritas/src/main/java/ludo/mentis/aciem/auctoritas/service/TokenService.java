package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.AuthenticationException;

public interface TokenService {
    String generateToken(String username, String[] roles) throws AuthenticationException, JOSEException;
    boolean validateToken(String token);
}