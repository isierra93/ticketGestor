package com.soluciones.ticketgestor.dtos;

import java.time.LocalDateTime;

public class CommentDto {

    private Long id;
    private LocalDateTime createdAt;
    private String comment;
    private UserOwnerDto userOwnerDto;

    public CommentDto() {
    }

    public CommentDto(Long id, LocalDateTime createdAt, String comment, UserOwnerDto userOwnerDto) {
        this.id = id;
        this.createdAt = createdAt;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserOwnerDto getUserOwnerDto() {
        return userOwnerDto;
    }

    public void setUserOwnerDto(UserOwnerDto userOwnerDto) {
        this.userOwnerDto = userOwnerDto;
    }

}
