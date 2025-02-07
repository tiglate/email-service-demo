package ludo.mentis.aciem.commons.security.client;

import ludo.mentis.aciem.commons.security.model.PublicKeyResponse;
import ludo.mentis.aciem.commons.security.model.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "auctoritas")
public interface OAuthClient {

    @PostMapping("/oauth/token")
    TokenResponse getToken(@RequestParam String username, @RequestParam String password);

    @GetMapping("/oauth/certs")
    PublicKeyResponse getPublicKey();

    @GetMapping("/oauth/validate")
    Map<String, String> validateToken(@RequestParam String token);
}