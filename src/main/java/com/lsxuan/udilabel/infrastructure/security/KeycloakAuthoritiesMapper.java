package com.lsxuan.udilabel.infrastructure.security;

import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Keycloak 权限映射器。
 *
 * 将 Keycloak Client Roles 映射为 Spring Security GrantedAuthority。
 */
@Component
public class KeycloakAuthoritiesMapper implements GrantedAuthoritiesMapper {

    /**
     * Keycloak Client ID。
     */
    private static final String CLIENT_ID = "x-udi-label";

    /**
     * Keycloak Client Roles Claim。
     */
    private static final String RESOURCE_ACCESS = "resource_access";

    private static final String ROLES = "roles";

    @Override
    public @NonNull Collection<? extends GrantedAuthority> mapAuthorities(
        @NonNull Collection<? extends GrantedAuthority> authorities) {

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>(authorities);

        for (GrantedAuthority authority : authorities) {

            if (!(authority instanceof OidcUserAuthority oidcAuthority)) {
                continue;
            }

            Map<String, Object> claims = oidcAuthority.getIdToken().getClaims();

            Object resourceAccess = claims.get(RESOURCE_ACCESS);

            if (!(resourceAccess instanceof Map<?, ?> resourceMap)) {
                continue;
            }

            Object clientAccess = resourceMap.get(CLIENT_ID);

            if (!(clientAccess instanceof Map<?, ?> clientMap)) {
                continue;
            }

            Object roles = clientMap.get(ROLES);

            if (!(roles instanceof Collection<?> roleList)) {
                continue;
            }

            for (Object role : roleList) {

                if (role instanceof String roleName) {
                    mappedAuthorities.add(new SimpleGrantedAuthority(roleName));
                }
            }
        }

        return mappedAuthorities;
    }
}