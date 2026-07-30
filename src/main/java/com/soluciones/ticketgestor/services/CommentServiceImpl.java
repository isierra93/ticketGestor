package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.exceptions.ResourceIncompleteException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Comment;
import com.soluciones.ticketgestor.repositories.CommentRepository;
import com.soluciones.ticketgestor.repositories.TicketRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TicketRepository ticketRepository) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Comment> getCommentsByTicketId(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("El TK ID: " + ticketId + " no existe.");
        }
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }

    @Override
    public Comment createComment(Long ticketId, Comment comment) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new ResourceNotFoundException("El TK ID: " + ticketId + " no existe.");
        }
        if (comment.getComment() == null || comment.getComment().isBlank()) {
            throw new ResourceIncompleteException("Campo incompleto: Comentario");
        }
        return commentRepository.save(comment);
    }

}
