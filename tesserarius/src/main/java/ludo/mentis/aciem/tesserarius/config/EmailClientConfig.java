package ludo.mentis.aciem.tesserarius.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import feign.RequestInterceptor;
import ludo.mentis.aciem.tesserarius.client.AuctoritasClient;
import ludo.mentis.aciem.tesserarius.model.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class EmailClientConfig {

    private final AuctoritasClient auctoritasClient;

    @Value("${tabellarius.service.username}")
    private String username;

    @Value("${tabellarius.service.password}")
    private String password;

    private final Cache<String, String> tokenCache;

    public EmailClientConfig(AuctoritasClient auctoritasClient) {
        this.auctoritasClient = auctoritasClient;
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
            TokenResponse response = auctoritasClient.getToken(username, password);
            return response.getTokenType() + " " + response.getAccessToken();
        });
    }
}