package com.lsxuan.udilabel.infrastructure.security;

import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class ApplicationOidcUser implements OidcUser {

    private final OidcUser delegate;

    private final UUID userId;

    public ApplicationOidcUser(OidcUser delegate, UUID userId) {

        if (delegate == null) {
            throw new IllegalArgumentException("OIDC user must not be null.");
        }

        if (userId == null) {
            throw new IllegalArgumentException("Internal user ID must not be null.");
        }

        this.delegate = delegate;
        this.userId = userId;
    }

    @Override
    public Map<String, Object> getClaims() {
        return delegate.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public @NonNull String getName() {
        return delegate.getName();
    }
}