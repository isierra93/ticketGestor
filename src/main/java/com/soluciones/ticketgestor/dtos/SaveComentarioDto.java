package com.soluciones.ticketgestor.dtos;

public class SaveComentarioDto {

    private String comentario;

    public SaveComentarioDto() {
    }

    public SaveComentarioDto(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}
