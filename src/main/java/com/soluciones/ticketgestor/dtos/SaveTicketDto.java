package com.soluciones.ticketgestor.dtos;


import com.soluciones.ticketgestor.models.Priority;

public class SaveTicketDto {
    private Long tkNumber;
    private String site;
    private Priority priority;
    private String description;
    private String type;

    public SaveTicketDto() {
    }

    public SaveTicketDto(Long tkNumber, String site, Priority priority, String description, String type) {
        this.tkNumber = tkNumber;
        this.site = site;
        this.priority = priority;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
