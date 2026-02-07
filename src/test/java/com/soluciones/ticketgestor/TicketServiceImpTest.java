package com.soluciones.ticketgestor;

import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.models.Priority;
import com.soluciones.ticketgestor.models.State;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.repositories.TicketRepository;
import com.soluciones.ticketgestor.services.TicketServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImpTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImp ticketService; // Asegúrate de que el nombre coincida con tu clase real

    @Test
    void createTicket_ShouldSaveTicket_WhenTkNumberDoesNotExist() {
        // GIVEN: Preparamos un ticket con todos los datos necesarios
        Ticket newTicket = new Ticket();
        newTicket.setTkNumber(100L);
        newTicket.setSite("Sucursal Test");
        newTicket.setPriority(Priority.ALTA);
        newTicket.setDescription("Descripción de prueba");
        newTicket.setType("Mantenimiento");
        // State y Date ya se ponen solos por defecto ;)

        // Entrenamos al Mock: "No existe este número, puedes guardar"
        when(ticketRepository.existsByTkNumber(100L)).thenReturn(false);
        when(ticketRepository.save(newTicket)).thenReturn(newTicket);

        // WHEN: Ejecutamos
        Ticket savedTicket = ticketService.createTicket(newTicket);

        // THEN: Verificamos
        assertNotNull(savedTicket);
        assertEquals(State.ABIERTO, savedTicket.getState()); // Verificamos que naciera ABIERTO
        verify(ticketRepository).save(newTicket);
    }

    @Test
    void createTicket_ShouldThrowException_WhenTkNumberExists(){
        //Given: Preparamos tk conflictivo
        Ticket duplicateTicket = new Ticket();
        duplicateTicket.setTkNumber(999L);
        duplicateTicket.setSite("Sucursal A");
        duplicateTicket.setPriority(Priority.MEDIA);
        duplicateTicket.setDescription("Ticket duplicado");
        duplicateTicket.setType("Soporte");
        //State y Date son automáticos

        //Entrenamos al Mock: "Si, ese numero ya existe!"
        when(ticketRepository.existsByTkNumber(999L)).thenReturn(true);

        //When & THEN: Verificamos que lanza la excepcion
        assertThrows(ResourceAlreadyExistsException.class, () -> {
           ticketService.createTicket(duplicateTicket);
        });

        //Verificamos que el repositorio NUNCA haya intentado guardar nada
        verify(ticketRepository, never()).save(any());

    }
}