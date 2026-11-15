package com.eaa.product_app.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Scope names expected on the JWT's "scope"/"scp" claim (auto-prefixed "SCOPE_" by
    // Spring's default JwtAuthenticationConverter). Must be granted to clients/users on
    // the Authorization-Server for these rules to have any effect.
    private static final String SCOPE_ADMIN = "SCOPE_catalog:admin"; // manage category/sub-category schemas
    private static final String SCOPE_WRITE = "SCOPE_catalog:write"; // create/update/delete products
    private static final String SCOPE_READ = "SCOPE_catalog:read";   // browse categories/sub-categories/products

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                // Stateless API: no HTTP session; each request is authenticated by its JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> c
                        // Schema management: only catalog admins may create categories/sub-categories
                        // (they are create+read-only entities; no update/delete endpoints exist).
                        .requestMatchers(HttpMethod.POST, "/categories/**", "/subcategories/**")
                        .hasAuthority(SCOPE_ADMIN)
                        // Product catalog management: admins or catalog writers
                        .requestMatchers(HttpMethod.POST, "/products/**")
                        .hasAnyAuthority(SCOPE_ADMIN, SCOPE_WRITE)
                        .requestMatchers(HttpMethod.PUT, "/products/**")
                        .hasAnyAuthority(SCOPE_ADMIN, SCOPE_WRITE)
                        .requestMatchers(HttpMethod.DELETE, "/products/**")
                        .hasAnyAuthority(SCOPE_ADMIN, SCOPE_WRITE)
                        // Browsing: any client with at least read access
                        .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**", "/subcategories/**")
                        .hasAnyAuthority(SCOPE_ADMIN, SCOPE_WRITE, SCOPE_READ)
                        // Everything else is denied by default
                        .anyRequest().denyAll())
                // Default JWT authentication; no custom claim mapping needed for now
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
                .build();
    }
}
