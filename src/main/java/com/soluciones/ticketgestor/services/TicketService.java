package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> getTicketList();
    Ticket getTicketById(Long id);
    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Ticket ticket);
    void deleteTicket(Long id);
    void validateNulls(Ticket ticket);
}
