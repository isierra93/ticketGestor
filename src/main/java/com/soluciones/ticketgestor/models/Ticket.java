package com.soluciones.ticketgestor.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(prefix = {"ticket", ""})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tkNumber;
    private String site;

    @Enumerated(EnumType.STRING)
    private TicketPriority ticketPriority;

    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TicketState ticketState = TicketState.ABIERTO;

    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
