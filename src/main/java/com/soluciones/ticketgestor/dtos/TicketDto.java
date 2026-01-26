package com.soluciones.ticketgestor.dtos;

import com.soluciones.ticketgestor.models.Priority;
import com.soluciones.ticketgestor.models.State;

import java.time.LocalDateTime;

public class TicketDto {
    private Long tkNumber;
    private String site;
    private Priority priority;
    private LocalDateTime createdDate;
    private String description;
    private State state;
    private String type;

    public TicketDto(){
    }

    public TicketDto(Long tkNumber, String site, Priority priority, LocalDateTime createdDate, String description, State state, String type) {
        this.tkNumber = tkNumber;
        this.site = site;
        this.priority = priority;
        this.createdDate = createdDate;
        this.description = description;
        this.state = state;
        this.type = type;
    }

    public Long getTkNumber() {
        return tkNumber;
    }

    public void setTkNumber(Long tkNumber) {
        this.tkNumber = tkNumber;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
