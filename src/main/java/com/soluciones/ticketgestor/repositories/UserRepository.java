package com.soluciones.ticketgestor.repositories;

import com.soluciones.ticketgestor.models.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
