package com.soluciones.ticketgestor.models;

public enum TicketCategory {
    LUMINARIAS("LUMINARIAS"),
    GRUPO_ELECTROGENO("GRUPO_ELECTROGENO"),
    DESMALEZADO("DESMALEZADO"),
    BANCO_BATERIAS("BANCO_BATERIAS"),
    AIRE_ACONDICIONADO("AIRE_ACONDICIONADO");

    private String description;

    TicketCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
