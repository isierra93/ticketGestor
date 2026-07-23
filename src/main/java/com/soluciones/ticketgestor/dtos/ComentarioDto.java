package com.soluciones.ticketgestor.dtos;

import java.time.LocalDateTime;

public class ComentarioDto {

    private Long id;
    private LocalDateTime createdAt;
    private String comentario;
    private UserOwnerDto userOwnerDto;

    public ComentarioDto() {
    }

    public ComentarioDto(Long id, LocalDateTime createdAt, String comentario, UserOwnerDto userOwnerDto) {
        this.id = id;
        this.createdAt = createdAt;
        this.comentario = comentario;
        this.userOwnerDto = userOwnerDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public UserOwnerDto getUserOwnerDto() {
        return userOwnerDto;
    }

    public void setUserOwnerDto(UserOwnerDto userOwnerDto) {
        this.userOwnerDto = userOwnerDto;
    }

}
