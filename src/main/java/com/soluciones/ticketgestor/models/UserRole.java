package com.soluciones.ticketgestor.models;

public enum UserRole {

    ADMIN("ADMIN"),
    USER("USER");

    private String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
