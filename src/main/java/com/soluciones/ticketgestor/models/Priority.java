package com.soluciones.ticketgestor.models;

public enum Priority {
    URGENTE("URGENTE"),
    ALTA("ALTA"),
    MEDIA("MEDIA"),
    BAJA("BAJA");

    private String description;

    Priority(String descripcion) {
        this.description = descripcion;
    }

    public String getDescripcion() {
        return description;
    }
}
