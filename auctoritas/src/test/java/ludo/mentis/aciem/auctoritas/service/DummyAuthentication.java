package ludo.mentis.aciem.auctoritas.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class DummyAuthentication implements Authentication {

    @Serial
    private static final long serialVersionUID = -5234212961819195439L;

    private final String name;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated = true;

    public DummyAuthentication(String name, Collection<? extends GrantedAuthority> authorities) {
        this.name = name;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return name;
    }
}