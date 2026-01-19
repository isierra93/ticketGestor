package com.soluciones.ticketgestor.dtos;

import java.time.LocalDateTime;

public class ErrorDto {
    private String message;
    private String error;
    private Integer status;
    private LocalDateTime timestamp;

    public ErrorDto() {
    }

    public ErrorDto(String message, String error, Integer status, LocalDateTime timestamp) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
