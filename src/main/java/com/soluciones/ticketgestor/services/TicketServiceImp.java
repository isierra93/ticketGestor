package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.exceptions.ResourceIncompleteException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class TicketServiceImp implements TicketService{

    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getTicketList() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El TK ID: " + id + " no existe."));
    }

    @Override
    public void deleteTicket(Long id) {
        Ticket ticket = getTicketById(id);
        ticketRepository.deleteById(ticket.getId());
    }


    @Override
    public Ticket createTicket(Ticket ticket){
        this.validateNulls(ticket);
        if (ticketRepository.existsByTkNumber(ticket.getTkNumber())){
            throw new ResourceAlreadyExistsException("El Tk Number: " + ticket.getTkNumber() +" ya existe.");
        }
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket updateTicket(Ticket ticket){
        this.validateNulls(ticket);

        if (ticketRepository.existsByTkNumber(ticket.getTkNumber())){
            throw new ResourceAlreadyExistsException("El Tk Number: " + ticket.getTkNumber() +" ya existe.");
        }

        return ticketRepository.save(ticket);
    }


    public void validateNulls(Ticket ticket){
        if (ticket.getTkNumber() == null){
            throw new ResourceIncompleteException("Campo incompleto: Tk Number");
        }
        if (ticket.getSite() == null){
            throw new ResourceIncompleteException("Campo incompleto: Sitio");
        }
        if (ticket.getPriority() == null){
            throw new ResourceIncompleteException("Campo incompleto: Prioridad");
        }
        if (ticket.getDescription() == null){
            throw new ResourceIncompleteException("Campo incompleto: Description");
        }
        if (ticket.getState() == null){
            throw new ResourceIncompleteException("Campo incompleto: Estado.");
        }
        if (ticket.getType() == null){
            throw new ResourceIncompleteException("Campo incompleto: Tipo");
        }
    }


}
