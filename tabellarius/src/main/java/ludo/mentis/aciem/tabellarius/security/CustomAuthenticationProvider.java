package ludo.mentis.aciem.tabellarius.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final RestTemplate restTemplate;
    private final String authUrl;
    private final RSAPublicKey publicKey;

    public CustomAuthenticationProvider(String authUrl, RSAPublicKey publicKey) {
        this.restTemplate = new RestTemplate();
        this.authUrl = authUrl;
        this.publicKey = publicKey;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        var password = authentication.getCredentials().toString();

        try {
            var token = authenticateWithService(username, password);
            var signedJWT = parseToken(token);
            var authorities = extractAuthorities(signedJWT);

            var userDetails = new User(username, password, authorities);
            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (UsernameNotFoundException | ParseException | JOSEException e) {
            throw new AuthenticationException("Authentication failed", e) {
            };
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private String authenticateWithService(String username, String password) {
        var url = authUrl + "/oauth/token";
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("username", username);
        requestBody.add("password", password);
        var request = new HttpEntity<>(requestBody, headers);

        try {
            var response = restTemplate.postForEntity(url, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                var body = response.getBody();
                if (body == null) {
                    throw new UsernameNotFoundException("JWT token body is empty");
                }
                return (String) body.get("access_token");
            } else {
                throw new UsernameNotFoundException("Invalid credentials. Error code: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new UsernameNotFoundException("Invalid credentials", e);
        }
    }

    private SignedJWT parseToken(String token) throws ParseException, JOSEException {
        var signedJWT = SignedJWT.parse(token);
        var verifier = new RSASSAVerifier(publicKey);
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Invalid token signature");
        }
        return signedJWT;
    }

    private List<SimpleGrantedAuthority> extractAuthorities(SignedJWT signedJWT) throws ParseException {
        var claims = signedJWT.getJWTClaimsSet();
        var rolesClaim = claims.getClaim("roles");
        if (!(rolesClaim instanceof List<?>)) {
            throw new ParseException("Invalid roles claim", 0);
        }
        var roles = (List<?>) rolesClaim;
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.toString()))
                .collect(Collectors.toList());
    }
}