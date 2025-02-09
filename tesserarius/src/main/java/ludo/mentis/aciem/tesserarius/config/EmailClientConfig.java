package ludo.mentis.aciem.tesserarius.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jwt.SignedJWT;
import feign.RequestInterceptor;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Configuration
public class EmailClientConfig {

    private static final Logger log = LoggerFactory.getLogger(EmailClientConfig.class);

    private final OAuthService oauthService;

    @Value("${tabellarius.service.username}")
    private String username;

    @Value("${tabellarius.service.password}")
    private String password;

    private final Cache<String, String> tokenCache;

    public EmailClientConfig(OAuthService oauthService) {
        this.oauthService = oauthService;
        this.tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", getBearerToken());
    }

    private String getBearerToken() {
        return tokenCache.get("token", key -> {
            SignedJWT response = null;
            try {
                response = oauthService.getToken(username, password);
                return "Bearer " + response.serialize();
            } catch (ParseException | PublicKeyException | InvalidSignatureException e) {
                log.error("Error while getting token", e);
            }
            return "";
        });
    }
}