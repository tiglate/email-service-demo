package ludo.mentis.aciem.commons.security;

import com.nimbusds.jwt.SignedJWT;
import ludo.mentis.aciem.commons.security.exception.InvalidSignatureException;
import ludo.mentis.aciem.commons.security.exception.PublicKeyException;
import ludo.mentis.aciem.commons.security.service.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private OAuthService oauthService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConstructor() {
        var filter = new JwtAuthenticationFilter(oauthService);
        assertNotNull(filter);
    }

    @Test
    void doFilterInternal_ValidToken() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer validToken");

        var signedJWT = mock(SignedJWT.class);
        var username = "testUser";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        when(oauthService.parseToken("validToken")).thenReturn(signedJWT);
        when(oauthService.extractUsername(signedJWT)).thenReturn(username);
        when(oauthService.extractAuthorities(signedJWT)).thenReturn(authorities);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getPrincipal());
        assertEquals(authorities, authentication.getAuthorities());
        assertTrue(authentication.isAuthenticated());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidToken() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalidToken");

        when(oauthService.parseToken("invalidToken")).thenThrow(new ParseException("Parse error", 0));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_InvalidSignature() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer invalidSignatureToken");

        when(oauthService.parseToken("invalidSignatureToken")).thenThrow(new InvalidSignatureException("Invalid signature"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_PublicKeyException() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer publicKeyExceptionToken");

        when(oauthService.parseToken("publicKeyExceptionToken")).thenThrow(new PublicKeyException("Public key error"));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader() throws Exception {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_UnableToExtractUsername() throws InvalidSignatureException, ParseException, PublicKeyException, ServletException, IOException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer validToken");

        var signedJWT = mock(SignedJWT.class);

        when(oauthService.parseToken("validToken")).thenReturn(signedJWT);
        when(oauthService.extractUsername(signedJWT)).thenThrow(new ParseException("Parse error", 0));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unable to parse the username from the provided token.", response.getErrorMessage());
    }

    @Test
    void doFilterInternal_UnableToExtractRoles() throws ParseException, ServletException, IOException, InvalidSignatureException, PublicKeyException {
        var request = new MockHttpServletRequest();
        var response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer validToken");

        var signedJWT = mock(SignedJWT.class);

        when(oauthService.parseToken("validToken")).thenReturn(signedJWT);
        when(oauthService.extractUsername(signedJWT)).thenReturn("testUser");
        when(oauthService.extractAuthorities(signedJWT)).thenThrow(new ParseException("Parse error", 0));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Unable to parse the authorities from the provided token.", response.getErrorMessage());
    }
}