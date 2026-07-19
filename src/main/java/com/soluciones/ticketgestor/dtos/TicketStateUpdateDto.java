package com.soluciones.ticketgestor.dtos;

import com.soluciones.ticketgestor.models.TicketState;

public class TicketStateUpdateDto {

    private TicketState state;

    public TicketStateUpdateDto() {
    }

    public TicketStateUpdateDto(TicketState state) {
        this.state = state;
    }

    public TicketState getState() {
        return state;
    }

    public void setState(TicketState state) {
        this.state = state;
    }
}
