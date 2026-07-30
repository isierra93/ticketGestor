package com.soluciones.ticketgestor.dtos;


import com.soluciones.ticketgestor.models.TicketCategory;
import com.soluciones.ticketgestor.models.TicketPriority;

public class SaveTicketDto {
    private Long tkNumber;
    private String site;
    private TicketPriority ticketPriority;
    private String description;
    private TicketCategory category;

    public SaveTicketDto() {
    }

    public SaveTicketDto(Long tkNumber, String site, TicketPriority ticketPriority, String description, TicketCategory category) {
        this.tkNumber = tkNumber;
        this.site = site;
        this.ticketPriority = ticketPriority;
        this.description = description;
        this.category = category;
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

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }
}
