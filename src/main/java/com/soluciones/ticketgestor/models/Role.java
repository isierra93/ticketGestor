package com.soluciones.ticketgestor.models;

public enum Role {

    ADMIN("ADMIN"),
    USER("USER");

    private String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
