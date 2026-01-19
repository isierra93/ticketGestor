package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.SaveTicketDto;
import com.soluciones.ticketgestor.dtos.TicketDto;
import com.soluciones.ticketgestor.models.Ticket;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketMapper {

    public TicketDto toDto(Ticket ticket){

        return new TicketDto(
                ticket.getTkNumber(),
                ticket.getSite(),
                ticket.getPriority(),
                ticket.getCreatedDate(),
                ticket.getDescription(),
                ticket.getState(),
                ticket.getType());
    }

    public Ticket toEntity(SaveTicketDto saveDto) {
        Ticket ticket = new Ticket();
        ticket.setTkNumber(saveDto.getTkNumber());
        ticket.setSite(saveDto.getSite());
        ticket.setPriority(saveDto.getPriority());
        ticket.setDescription(saveDto.getDescription());
        ticket.setType(saveDto.getType());

        // Valores por defecto
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setState("ABIERTO");

        return ticket;
    }

    public void updateTicketFromDto(TicketDto dto, Ticket entity){
        entity.setSite(dto.getSite());
        entity.setPriority(dto.getPriority());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setState(dto.getState());
    }

}
