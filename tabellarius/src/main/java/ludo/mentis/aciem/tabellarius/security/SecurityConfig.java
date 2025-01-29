package ludo.mentis.aciem.tabellarius.security;

import ludo.mentis.aciem.tabellarius.service.PublicKeyService;
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
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringAntMatchers("/actuator/**", "/api/**"))
            .authorizeRequests(authorize -> authorize
                .antMatchers("/swagger-ui.html",
                             "/swagger-ui/**",
                             "/v3/api-docs/**",
                             "/login",
                             "/static/**",
                             "/webjars/**",
                             "/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilter(jwtAuthenticationFilter)
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?loginError=true"))
            .logout(logout -> logout
                .logoutSuccessUrl("/?logoutSuccess=true")
                .deleteCookies("JSESSIONID"))
            .exceptionHandling(exception -> exception
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), request -> request.getRequestURI().startsWith("/api/"))
                .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true"), request -> !request.getRequestURI().startsWith("/api/")));

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider(PublicKeyService publicKeyService, RestTemplate restTemplate) {
        return new CustomAuthenticationProvider(publicKeyService, restTemplate);
    }
}

