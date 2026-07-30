package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getCommentsByTicketId(Long ticketId);

    Comment createComment(Long ticketId, Comment comment);

}
