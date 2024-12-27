package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

public interface AuthService {
    Map<String, String> generateToken(String username, String password) throws AuthenticationException, JOSEException;
    boolean validateToken(String token);
    String getPublicKey();
}