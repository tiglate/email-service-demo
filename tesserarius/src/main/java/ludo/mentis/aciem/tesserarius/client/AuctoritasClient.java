package ludo.mentis.aciem.tesserarius.client;

import ludo.mentis.aciem.tesserarius.model.PublicKeyResponse;
import ludo.mentis.aciem.tesserarius.model.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auctoritas")
public interface AuctoritasClient {

    @PostMapping("/oauth/token")
    TokenResponse getToken(@RequestParam String username, @RequestParam String password);

    @GetMapping("/oauth/certs")
    PublicKeyResponse getPublicKey();
}