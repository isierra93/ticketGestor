package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.SaveTicketDto;
import com.soluciones.ticketgestor.dtos.TicketDto;
import com.soluciones.ticketgestor.mappers.TicketMapper;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.soluciones.ticketgestor.services.TicketService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Operation(
            summary = "Obtener todos los tickets.",
            description = "Devuelve una lista completa de todos los tickets existentes en " +
                    "el sistema representados como DTOs."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Lista de tickets obtenida correctamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TicketDto.class))
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<TicketDto>> getTickets(){

        //Convertimos a ticketDto antes de responder
        List<TicketDto> ticketDtoList = ticketService.getTicketList()
                .stream()
                .map(ticket -> ticketMapper.toDto(ticket))
                .toList();
        return ResponseEntity.ok(ticketDtoList);
    }

    @Operation(
            summary = "Obtener un ticket.",
            description = "Devuelve un ticket por su id representado como un DTO."
    )

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id){
        Ticket ticket = ticketService.getTicketById(id);
        TicketDto ticketDto = ticketMapper.toDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @Operation(
            summary = "Agregar un ticket.",
            description = "Agrega un ticket a la base de datos, registrando el usuario que lo creo."
    )
    @PostMapping
    public ResponseEntity<TicketDto> postTicket(
            @RequestBody SaveTicketDto saveTicketDto,
            @AuthenticationPrincipal UserDetails userDetails){

        User user = userServiceImpl.getUserByEmail(userDetails.getUsername());
        Ticket ticketEntity = ticketMapper.toEntity(saveTicketDto, user);

        Ticket savedTicket = ticketService.createTicket(ticketEntity);
        TicketDto ticketDto = ticketMapper.toDto(savedTicket);

        //Crea la URI para devolver en el created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTicket.getId())
                .toUri();

        return ResponseEntity.created(location).body(ticketDto);
    }

    @Operation(
            summary = "Actualizar un ticket.",
            description = "Actualizar un ticket por su id en la base de datos."
    )
    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> putTicket(@PathVariable Long id, @RequestBody TicketDto dto){
        Ticket existingTicket = ticketService.getTicketById(id);
        ticketMapper.updateTicketFromDto(dto, existingTicket);
        Ticket savedTicket = ticketService.updateTicket(existingTicket);
        return ResponseEntity.ok(ticketMapper.toDto(savedTicket));
    }

    @Operation(
            summary = "Eliminar un ticket.",
            description = "Elimina un ticket por su id de la base de datos."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

}
