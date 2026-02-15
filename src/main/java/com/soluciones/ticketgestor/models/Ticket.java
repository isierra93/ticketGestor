package com.soluciones.ticketgestor.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tkNumber;
    private String site;

    @Enumerated(EnumType.STRING)
    private TicketPriority ticketPriority;

    private LocalDateTime createdDate = LocalDateTime.now();
    private String description;

    @Enumerated(EnumType.STRING)
    private TicketState ticketState = TicketState.ABIERTO;

    private String type;

    public Ticket() {
    }

    public Ticket(Long id, Long tkNumber, String site, TicketPriority ticketPriority, LocalDateTime createdDate, String description, TicketState ticketState, String type) {
        this.id = id;
        this.tkNumber = tkNumber;
        this.site = site;
        this.ticketPriority = ticketPriority;
        this.createdDate = createdDate;
        this.description = description;
        this.ticketState = ticketState;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
