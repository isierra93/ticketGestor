package com.soluciones.ticketgestor.models;

public enum Priority {
    URGENTE("URGENTE"),
    ALTA("ALTA"),
    MEDIA("MEDIA"),
    BAJA("BAJA");

    private String descripcion;

    Priority(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
