package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.ErrorDto;
import com.soluciones.ticketgestor.dtos.SaveTicketDto;
import com.soluciones.ticketgestor.dtos.TicketDto;
import com.soluciones.ticketgestor.mappers.TicketMapper;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.soluciones.ticketgestor.services.TicketService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
@Tag(name = "Ticket Controller", description = "Endpoints para el manejo de tickets.")
@SecurityRequirement(name = "bearerAuth")
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
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Se requiere un token JWT válido para realizar esta acción.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-01T16:32:46.097Z"
                                                    }
                                                    """
                                    )
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
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Ticket obtenido correctamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Se requiere un token JWT válido para realizar esta acción.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-01T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Operación fallida. Ticket no encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "El TK ID: 9999 no existe.",
                                                      "error": "Not Found",
                                                      "status": 404,
                                                      "timestamp": "2026-02-28T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )

                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable
            @Parameter(description = "El número identificador único del ticket a buscar.")
            Long id ){

        Ticket ticket = ticketService.getTicketById(id);
        TicketDto ticketDto = ticketMapper.toDto(ticket);
        return ResponseEntity.ok(ticketDto);
    }

    @Operation(
            summary = "Agregar un ticket.",
            description = "Agrega un ticket a la base de datos, registrando el usuario que lo creo."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Operación exitosa. Ticket creado correctamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos. Verifica el formato del JSON enviado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "Error de formato en el JSON.",
                                                      "error": "Bad Request",
                                                      "status": 400,
                                                      "timestamp": "2026-02-28T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Se requiere un token JWT válido para realizar esta acción.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-01T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
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
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Operación exitosa. Ticket actualizado correctamente.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDto.class)
                            )
                    )
                    ,
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos. Verifica el formato del JSON enviado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "Error de formato en el JSON.",
                                                      "error": "Bad Request",
                                                      "status": 400,
                                                      "timestamp": "2026-02-28T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )

                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Se requiere un token JWT válido para realizar esta acción.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-01T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Operación fallida. Ticket no encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "El TK ID: 9999 no existe.",
                                                      "error": "Not Found",
                                                      "status": 404,
                                                      "timestamp": "2026-02-28T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )

                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Operación fallida. El recurso se encuentra duplicado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "El Tk Number: 1234 ya existe.",
                                                      "error": "Conflict",
                                                      "status": 409,
                                                      "timestamp": "2026-02-28T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )

                    )
            }
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
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Operación exitosa. Ticket actualizado correctamente."
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Se requiere un token JWT válido para realizar esta acción.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "No autorizado. Se requiere un token JWT válido para acceder a este recurso.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-01T16:32:46.097Z"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

}
