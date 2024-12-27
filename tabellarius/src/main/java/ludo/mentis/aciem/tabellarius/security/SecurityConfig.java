package ludo.mentis.aciem.tabellarius.security;

import ludo.mentis.aciem.tabellarius.util.KeyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.security.PublicKey;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PublicKey publicKey) throws Exception {
        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/actuator/**", "/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilter(new JwtAuthenticationFilter(publicKey))
                .build();
    }

    @Bean
    public PublicKey publicKey() throws PublicKeyException {
        var certsUrl = "http://localhost:9000/oauth/certs";
        return KeyUtils.getPublicKeyFromCertsEndpoint(certsUrl);
    }
}

