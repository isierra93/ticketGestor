package com.soluciones.ticketgestor.dtos;

public class UserOwnerDto {
    private String email;

    public UserOwnerDto() {
    }

    public UserOwnerDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
