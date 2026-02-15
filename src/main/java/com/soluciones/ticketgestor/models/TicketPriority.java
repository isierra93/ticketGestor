package com.soluciones.ticketgestor.models;

public enum TicketPriority {
    URGENTE("URGENTE"),
    ALTA("ALTA"),
    MEDIA("MEDIA"),
    BAJA("BAJA");

    private String description;

    TicketPriority(String descripcion) {
        this.description = descripcion;
    }

    public String getDescripcion() {
        return description;
    }
}
