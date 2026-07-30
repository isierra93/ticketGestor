package com.soluciones.ticketgestor.dtos;

import com.soluciones.ticketgestor.models.TicketCategory;
import com.soluciones.ticketgestor.models.TicketPriority;
import com.soluciones.ticketgestor.models.TicketState;

import java.time.LocalDateTime;

public class TicketDto {
    private Long tkNumber;
    private String site;
    private TicketPriority ticketPriority;
    private LocalDateTime createdDate;
    private String description;
    private TicketState ticketState;
    private TicketCategory category;
    private UserOwnerDto userOwnerDto;

    public TicketDto(){
    }

    public TicketDto(Long tkNumber, String site, TicketPriority ticketPriority, LocalDateTime createdDate,
                     String description, TicketState ticketState, TicketCategory category, UserOwnerDto userOwnerDto) {
        this.tkNumber = tkNumber;
        this.site = site;
        this.ticketPriority = ticketPriority;
        this.createdDate = createdDate;
        this.description = description;
        this.ticketState = ticketState;
        this.category = category;
        this.userOwnerDto = userOwnerDto;
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

    public TicketState getState() {
        return ticketState;
    }

    public void setState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public UserOwnerDto getUserOwnerDto() {
        return userOwnerDto;
    }

    public void setUserOwnerDto(UserOwnerDto userOwnerDto) {
        this.userOwnerDto = userOwnerDto;
    }
}
