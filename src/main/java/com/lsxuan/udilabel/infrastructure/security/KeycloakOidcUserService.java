package com.lsxuan.udilabel.infrastructure.security;

import com.lsxuan.udilabel.application.user.ExternalIdentityInfo;
import com.lsxuan.udilabel.application.user.UserApplicationService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KeycloakOidcUserService extends OidcUserService {

    private final UserApplicationService userApplicationService;

    public KeycloakOidcUserService(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. Load the authenticated OIDC user.
        OidcUser oidcUser = super.loadUser(userRequest);

        // 2. Extract identity information from the ID Token.
        String issuer = oidcUser.getIdToken().getIssuer().toString();

        String subject = oidcUser.getSubject();

        String email = oidcUser.getEmail();

        String displayName = oidcUser.getFullName();

        // Fallback when the full name claim is unavailable.
        if (displayName == null || displayName.isBlank()) {
            displayName = oidcUser.getPreferredUsername();
        }

        if (displayName == null || displayName.isBlank()) {
            displayName = email;
        }

        boolean emailVerified = Boolean.TRUE.equals(oidcUser.getEmailVerified());

        // 3. Build external identity information.
        ExternalIdentityInfo identityInfo =
            new ExternalIdentityInfo(issuer, subject, email, displayName, emailVerified);

        // 4. Create or retrieve the internal application user.
        UUID internalUserId;

        try {
            internalUserId = userApplicationService.getOrCreateUser(identityInfo);
        } catch (Exception exception) {
            OAuth2Error error =
                new OAuth2Error("user_provisioning_failed", "Failed to provision the application user.", null);

            throw new OAuth2AuthenticationException(error, "Unable to provision the application user.", exception);
        }

        // 5. Return an OIDC principal containing the internal user ID.
        return new ApplicationOidcUser(oidcUser, internalUserId);
    }
}