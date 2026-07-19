package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.models.TicketState;

import java.util.List;

public interface TicketService {
    List<Ticket> getTicketList();
    Ticket getTicketById(Long id);
    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Ticket ticket);
    Ticket updateTicketState(Long id, TicketState newState);
    void deleteTicket(Long id);
    void validateNulls(Ticket ticket);
}
