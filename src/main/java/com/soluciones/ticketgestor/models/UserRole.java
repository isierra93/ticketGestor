package com.soluciones.ticketgestor.models;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    ADMIN("ROLE_ADMIN"),
    AGENT("ROLE_AGENT"),
    CLIENT("ROLE_CLIENT");

    private String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public @Nullable String getAuthority() {
        return this.getDescription();
    }
}
