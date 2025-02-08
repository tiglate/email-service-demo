package ludo.mentis.aciem.commons.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationTokenTest {

    @Test
    void testGetPrincipal() {
        var principal = "testUser";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var token = new JwtAuthenticationToken(principal, authorities);

        assertEquals(principal, token.getPrincipal());
    }

    @Test
    void testGetCredentials() {
        var principal = "testUser";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var token = new JwtAuthenticationToken(principal, authorities);

        assertNull(token.getCredentials());
    }

    @Test
    void testIsAuthenticated() {
        var principal = "testUser";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var token = new JwtAuthenticationToken(principal, authorities);

        assertTrue(token.isAuthenticated());
    }

    @Test
    void testEqualsAndHashCode() {
        var principal = "testUser";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var token1 = new JwtAuthenticationToken(principal, authorities);
        var token2 = new JwtAuthenticationToken(principal, authorities);

        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void testNotEquals() {
        var principal1 = "testUser1";
        var principal2 = "testUser2";
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var token1 = new JwtAuthenticationToken(principal1, authorities);
        var token2 = new JwtAuthenticationToken(principal2, authorities);

        assertNotEquals(token1, token2);
    }
}