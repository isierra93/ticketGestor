package com.soluciones.ticketgestor.repositories;

import com.soluciones.ticketgestor.models.Ticket;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByTkNumber(Long tkNumber);
}
