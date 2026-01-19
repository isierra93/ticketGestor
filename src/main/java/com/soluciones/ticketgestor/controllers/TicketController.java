package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.SaveTicketDto;
import com.soluciones.ticketgestor.dtos.TicketDto;
import com.soluciones.ticketgestor.mappers.TicketMapper;
import com.soluciones.ticketgestor.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.soluciones.ticketgestor.services.TicketService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketMapper ticketMapper;

    @GetMapping
    public ResponseEntity<List<TicketDto>> getTickets(){

        //Convertimos a ticketDto antes de responder
        List<TicketDto> ticketDtoList = ticketService.getTicketList()
                .stream()
                .map(ticket -> ticketMapper.toDto(ticket))
                .toList();
        return ResponseEntity.ok(ticketDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id){
        Ticket ticket = ticketService.getTicketById(id);
        TicketDto ticketDto = ticketMapper.toDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping
    public ResponseEntity<TicketDto> postTicket(@RequestBody SaveTicketDto saveTicketDtoTicket){
        //Recibo el tk y lo convierto en entidad
        Ticket ticketEntity = ticketMapper.toEntity(saveTicketDtoTicket);
        //Guardo en DB el ticketEntiy
        Ticket savedTicket = ticketService.createTicket(ticketEntity);

        //Si se creo correctamente continua el flujo, de lo contrario el GlobalHandler atrapa la excepción

        //Convierto en Dto el ticket guardado para devolverlo
        TicketDto ticketDto = ticketMapper.toDto(savedTicket);

        //Crea la URI para devolver en el created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTicket.getId())
                .toUri();

        return ResponseEntity.created(location).body(ticketDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<TicketDto> putTicket(@PathVariable Long id,@RequestBody TicketDto dto ){
        Ticket existingTicket = ticketService.getTicketById(id);
        ticketMapper.updateTicketFromDto(dto, existingTicket);
        Ticket savedTicket = ticketService.saveTicket(existingTicket);
        return ResponseEntity.ok(ticketMapper.toDto(savedTicket));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        if (ticketService.deleteTicket(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



}
