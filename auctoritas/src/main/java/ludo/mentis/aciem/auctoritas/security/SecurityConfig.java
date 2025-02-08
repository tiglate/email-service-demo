package ludo.mentis.aciem.auctoritas.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain formSecurityConfigFilterChain(final HttpSecurity http,
    		@Value("${app.security.remember-me-key}") final String rememberMeKey) throws Exception {
        return http
        		.cors(Customizer.withDefaults())
                .csrf(csrf ->
                        csrf.ignoringAntMatchers("/actuator/**", "/oauth/**", "/api/**"))
                .authorizeRequests(authorize ->
                	authorize.antMatchers(
                		"/swagger-ui.html",
                		"/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/login",
                        "/static/**",
                        "/webjars/**",
                        "/oauth/**",
                        "/actuator/**",
                        "/favicon.ico").permitAll()
                	.anyRequest().authenticated())
                .formLogin(form -> form
                    .loginPage("/login")
                    .failureUrl("/login?loginError=true"))
                .rememberMe(rememberMe -> rememberMe
                    .tokenValiditySeconds((int) Duration.ofDays(180).getSeconds())
                    .rememberMeParameter("rememberMe")
                    .key(rememberMeKey))
                .logout(logout -> logout
                    .logoutSuccessUrl("/?logoutSuccess=true")
                    .deleteCookies("JSESSIONID"))
                .exceptionHandling(exception -> exception
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        		request -> request.getRequestURI().startsWith("/oauth/") || request.getRequestURI().startsWith("/api/"))
                        .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login?loginRequired=true"),
                        		request -> !request.getRequestURI().startsWith("/oauth/") && !request.getRequestURI().startsWith("/api/")))
                .build();
    }
}