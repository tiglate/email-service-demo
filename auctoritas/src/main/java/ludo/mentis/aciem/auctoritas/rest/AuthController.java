package ludo.mentis.aciem.auctoritas.rest;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ludo.mentis.aciem.auctoritas.service.AuthService;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String username,
                                                             @RequestParam String password) {
        try {
            final var tokenResponse = authService.generateToken(username, password);
            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/certs")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        final var publicKeyString = authService.getPublicKey();
        return ResponseEntity.ok(Map.of("public_key", publicKeyString));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestParam String token) {
        final var isValid = authService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", "true"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", "false"));
        }
    }
}