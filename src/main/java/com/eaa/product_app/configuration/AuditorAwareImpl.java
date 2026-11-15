package com.eaa.product_app.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Resolves the current auditor from the request's authenticated JWT principal
 * (this app's security is OAuth2 resource server / JWT-based, unlike the
 * reference product-service, which reads a custom "loggedInUser" header).
 * Returns empty when the request is unauthenticated (e.g. local testing with permitAll).
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.ofNullable(authentication.getName());
    }
}
