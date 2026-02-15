package com.soluciones.ticketgestor.models;

public enum TicketState {
    ABIERTO("ABIERTO"),
    ASIGNADO("ASIGNADO"),
    EN_CURSO("EN_CURSO"),
    CERRADO("CERRADO");

    private String description;

    TicketState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
