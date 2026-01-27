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
    private Priority priority;

    private LocalDateTime createdDate = LocalDateTime.now();
    private String description;

    @Enumerated(EnumType.STRING)
    private State state = State.ABIERTO;

    private String type;

    public Ticket() {
    }

    public Ticket(Long id, Long tkNumber, String site, Priority priority, LocalDateTime createdDate, String description, State state, String type) {
        this.id = id;
        this.tkNumber = tkNumber;
        this.site = site;
        this.priority = priority;
        this.createdDate = createdDate;
        this.description = description;
        this.state = state;
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
