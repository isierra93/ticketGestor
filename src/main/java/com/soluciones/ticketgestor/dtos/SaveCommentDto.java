package com.soluciones.ticketgestor.dtos;

public class SaveCommentDto {

    private String comment;

    public SaveCommentDto() {
    }

    public SaveCommentDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
