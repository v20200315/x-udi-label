package com.lsxuan.udilabel.infrastructure.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityCurrentUserContext implements CurrentUserContext {

    @Override
    public UUID getCurrentUserId() {

        return UUID.fromString("0198f3c2-7a21-7000-8000-000000000001");
    }

    @Override
    public String getCurrentSubject() {

        OidcUser oidcUser = getCurrentOidcUser();

        return oidcUser.getSubject();
    }

    @Override
    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private OidcUser getCurrentOidcUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            throw new IllegalStateException("No authenticated OIDC user found");
        }

        return oidcUser;
    }
}