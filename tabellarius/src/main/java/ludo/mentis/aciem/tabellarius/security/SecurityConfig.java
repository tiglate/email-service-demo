package ludo.mentis.aciem.tabellarius.security;

import ludo.mentis.aciem.tabellarius.util.KeyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.auth.service.url}")
    private String authServiceUrl;

    @Value("${app.security.remember-me.key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http,
                                                   final PublicKey publicKey) throws Exception {

        return http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/actuator/**", "/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/login", "/static/**", "/webjars/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilter(new JwtAuthenticationFilter(publicKey))
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?loginError=true"))
                .logout(logout -> logout
                        .logoutSuccessUrl("/?logoutSuccess=true")
                        .deleteCookies("JSESSIONID"))
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), request -> request.getRequestURI().startsWith("/api/"))
                        .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true"), request -> !request.getRequestURI().startsWith("/api/")))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() throws PublicKeyException {
        return new CustomAuthenticationProvider(authServiceUrl, (RSAPublicKey) publicKey());
    }

    @Bean
    public PublicKey publicKey() throws PublicKeyException {
        var certsUrl = "http://localhost:9000/oauth/certs";
        return KeyUtils.getPublicKeyFromCertsEndpoint(certsUrl);
    }
}

