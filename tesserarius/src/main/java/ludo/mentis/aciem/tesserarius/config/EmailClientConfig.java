package ludo.mentis.aciem.tesserarius.config;

import feign.RequestInterceptor;
import ludo.mentis.aciem.tesserarius.client.AuctoritasClient;
import ludo.mentis.aciem.tesserarius.model.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailClientConfig {

    private final AuctoritasClient auctoritasClient;

    @Value("${tabellarius.service.username}")
    private String username;

    @Value("${tabellarius.service.password}")
    private String password;

    public EmailClientConfig(AuctoritasClient auctoritasClient) {
        this.auctoritasClient = auctoritasClient;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", getBearerToken());
    }

    private String getBearerToken() {
        TokenResponse response = auctoritasClient.getToken(username, password);
        return response.getTokenType() + " " + response.getAccessToken();
    }
}