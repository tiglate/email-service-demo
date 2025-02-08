package ludo.mentis.aciem.auctoritas.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyConfig {

    @Bean
    KeyPair keyPair() throws NoSuchAlgorithmException {
        final var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean
    PrivateKey privateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    @Bean
    PublicKey publicKey(KeyPair keyPair) {
        return keyPair.getPublic();
    }
}
