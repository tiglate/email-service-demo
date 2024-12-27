package ludo.mentis.aciem.auctoritas.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    private final TokenService tokenService;
    private final PublicKey publicKey;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(TokenService tokenService, PublicKey publicKey, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.publicKey = publicKey;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Map<String, String> generateToken(String username, String password) throws AuthenticationException, JOSEException {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.isAuthenticated()) {
            var roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toArray(String[]::new);
            var token = tokenService.generateToken(username, roles);
            return Map.of("access_token", token, "token_type", "Bearer");
        } else {
            throw new AuthenticationException("Invalid credentials") {};
        }
    }

    @Override
    public boolean validateToken(String token) {
        return tokenService.validateToken(token);
    }

    @Override
    public String getPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}