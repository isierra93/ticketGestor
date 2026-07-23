package com.soluciones.ticketgestor.repositories;

import com.soluciones.ticketgestor.models.Comentario;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTicketIdOrderByCreatedAtAsc(Long ticketId);

}
