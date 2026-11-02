package com.lsxuan.udilabel.interfaces.console;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class DebugController {

    @GetMapping("/debug/authorities")
    @ResponseBody
    public Map<String, Object> authorities(Authentication authentication) {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<String> allAuthorities =
            authorities.stream().map(GrantedAuthority::getAuthority).filter(Objects::nonNull).sorted().toList();

        List<String> roles = allAuthorities.stream().filter(authority -> authority.startsWith("role_")).toList();

        List<String> permissions = allAuthorities.stream().filter(authority -> !authority.startsWith("role_")).toList();

        return Map.of("username", authentication.getName(), "roles", roles, "permissions", permissions, "authorities",
            allAuthorities);
    }

    @GetMapping("/debug/oidc-claims")
    @ResponseBody
    public Map<String, Object> oidcClaims(Authentication authentication) {

        if (!(authentication.getPrincipal() instanceof OidcUser oidcUser)) {
            return Map.of("error", "Current principal is not OidcUser");
        }

        Map<String, Object> claims = oidcUser.getIdToken().getClaims();

        return Map.of("subject", oidcUser.getSubject(), "issuer", oidcUser.getIssuer().toString(), "claimNames",
            claims.keySet(), "resourceAccess", claims.getOrDefault("resource_access", Map.of()));
    }
}