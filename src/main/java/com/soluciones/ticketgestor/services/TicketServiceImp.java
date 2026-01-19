package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class TicketServiceImp implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> getTicketList() {
        return ticketRepository.getTicketList();
    }

    @Override
    public Ticket getTicketById(Long id) {
        Optional<Ticket> ticket = ticketRepository.getTicketById(id);
        if (ticket.isPresent()){
            return ticket.get();
        }
        throw new ResourceNotFoundException("El TK: " + id.toString() + " no existe.");
    }

    @Override
    public boolean deleteTicket(Long id) {
        return ticketRepository.deleteTicket(id);
    }


    @Override
    public Ticket saveTicket(Ticket ticket){
        if (ticket.getId() != null){
            //Es un tk existente, actualizar
            return ticketRepository.updateTicket(ticket);
        }

        //Es un ticket nuevo, validar y luego crear
        if (ticketRepository.getTicketByTkNumber(ticket.getTkNumber()).isPresent()){
            throw new ResourceAlreadyExistsException("El TK: " + ticket.getTkNumber().toString() + " ya existe.");
        }
        return ticketRepository.addTicket(ticket);
    }


    @Override
    public Ticket createTicket(Ticket ticket) {
        if (ticketRepository.getTicketByTkNumber(ticket.getTkNumber()).isPresent()){
            throw new ResourceAlreadyExistsException("El TK: " + ticket.getTkNumber().toString() + " ya existe.");
        }
        return ticketRepository.addTicket(ticket);
    }



}
