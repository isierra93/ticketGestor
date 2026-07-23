package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.exceptions.ResourceIncompleteException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Comentario;
import com.soluciones.ticketgestor.repositories.ComentarioRepository;
import com.soluciones.ticketgestor.repositories.TicketRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TicketRepository ticketRepository;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, TicketRepository ticketRepository) {
        this.comentarioRepository = comentarioRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Comentario> getComentariosByTicketId(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("El TK ID: " + ticketId + " no existe.");
        }
        return comentarioRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    @Override
    public Comentario createComentario(Long ticketId, Comentario comentario) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("El TK ID: " + ticketId + " no existe.");
        }
        if (comentario.getComentario() == null || comentario.getComentario().isBlank()) {
            throw new ResourceIncompleteException("Campo incompleto: Comentario");
        }
        return comentarioRepository.save(comentario);
    }

}
