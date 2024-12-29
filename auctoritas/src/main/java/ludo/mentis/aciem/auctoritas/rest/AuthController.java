package ludo.mentis.aciem.auctoritas.rest;

import ludo.mentis.aciem.auctoritas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String username,
                                                             @RequestParam String password) {
        try {
            var tokenResponse = authService.generateToken(username, password);
            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/certs")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        var publicKeyString = authService.getPublicKey();
        return ResponseEntity.ok(Map.of("public_key", publicKeyString));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateToken(@RequestParam String token) {
        var isValid = authService.validateToken(token);
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", "true"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", "false"));
        }
    }
}