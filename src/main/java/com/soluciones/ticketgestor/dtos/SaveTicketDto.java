package com.soluciones.ticketgestor.dtos;


import com.soluciones.ticketgestor.models.TicketPriority;

public class SaveTicketDto {
    private Long tkNumber;
    private String site;
    private TicketPriority ticketPriority;
    private String description;
    private String type;

    public SaveTicketDto() {
    }

    public SaveTicketDto(Long tkNumber, String site, TicketPriority ticketPriority, String description, String type) {
        this.tkNumber = tkNumber;
        this.site = site;
        this.ticketPriority = ticketPriority;
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

    public TicketPriority getPriority() {
        return ticketPriority;
    }

    public void setPriority(TicketPriority ticketPriority) {
        this.ticketPriority = ticketPriority;
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
