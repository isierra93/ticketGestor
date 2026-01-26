package com.soluciones.ticketgestor.models;

public enum State {
    ABIERTO("ABIERTO"),
    ASIGNADO("ASIGNADO"),
    EN_CURSO("EN_CURSO"),
    CERRADO("CERRADO");

    private String description;

    State(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
