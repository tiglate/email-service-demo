package ludo.mentis.aciem.commons.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -6368725567537680251L;
    private final String principal;

    public JwtAuthenticationToken(String principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null; // No credentials required for JWT
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        JwtAuthenticationToken that = (JwtAuthenticationToken) obj;
        return principal.equals(that.principal);
    }

    @Override
    public int hashCode() {
        return principal.hashCode();
    }
}
