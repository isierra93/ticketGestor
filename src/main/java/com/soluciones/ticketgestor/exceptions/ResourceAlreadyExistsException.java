package com.soluciones.ticketgestor.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
