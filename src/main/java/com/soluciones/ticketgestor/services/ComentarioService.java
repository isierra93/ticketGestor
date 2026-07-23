package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.Comentario;

import java.util.List;

public interface ComentarioService {

    List<Comentario> getComentariosByTicketId(Long ticketId);

    Comentario createComentario(Long ticketId, Comentario comentario);

}
