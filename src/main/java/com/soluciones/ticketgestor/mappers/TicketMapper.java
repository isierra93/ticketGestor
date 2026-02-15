package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.SaveTicketDto;
import com.soluciones.ticketgestor.dtos.TicketDto;
import com.soluciones.ticketgestor.models.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDto toDto(Ticket ticket){
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTkNumber(ticket.getTkNumber());
        ticketDto.setSite(ticket.getSite());
        ticketDto.setPriority(ticket.getPriority());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setType(ticket.getType());
        ticketDto.setState(ticket.getState());
        ticketDto.setCreatedDate(ticket.getCreatedDate());

        return ticketDto;
    }

    public Ticket toEntity(SaveTicketDto saveDto) {
        Ticket ticket = new Ticket();
        ticket.setTkNumber(saveDto.getTkNumber());
        ticket.setSite(saveDto.getSite());
        ticket.setPriority(saveDto.getPriority());
        ticket.setDescription(saveDto.getDescription());
        ticket.setType(saveDto.getType());

        return ticket;
    }

    public void updateTicketFromDto(TicketDto dto, Ticket entity){
        entity.setTkNumber(dto.getTkNumber());
        entity.setSite(dto.getSite());
        entity.setPriority(dto.getPriority());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setState(dto.getState());
    }

}
