package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Ticket> getTicketList();
    Ticket getTicketById(Long id);
    Ticket createTicket(Ticket ticket);
    boolean deleteTicket(Long id);

    //Este metodo reemplazara a create y update
    Ticket saveTicket(Ticket ticket);
}
