package com.soluciones.ticketgestor.repositories;

import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Ticket;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TicketRepository {

    private List<Ticket> ticketList = new ArrayList<>(List.of(
            new Ticket(1L, 1001L, "Sede Central", "ALTA", LocalDateTime.now().minusDays(5), "Fallo en servidor de correos", "ABIERTO", "INCIDENCIA"),
            new Ticket(2L, 1002L, "Sucursal Norte", "MEDIA", LocalDateTime.now().minusDays(2), "Solicitud de nuevo monitor", "PENDIENTE", "REQUERIMIENTO"),
            new Ticket(3L, 1003L, "Remoto", "BAJA", LocalDateTime.now().minusHours(5), "Actualización de antivirus", "CERRADO", "MANTENIMIENTO"),
            new Ticket(4L, 1004L, "Sede Central", "CRITICA", LocalDateTime.now(), "Caída de sistema de pagos", "EN_PROGRESO", "INCIDENCIA"),
            new Ticket(5L, 1005L, "Sucursal Sur", "MEDIA", LocalDateTime.now().minusDays(10), "Reinicio de router", "ABIERTO", "SOPORTE"),
            new Ticket(6L, 1006L, "Planta Baja", "ALTA", LocalDateTime.now().minusWeeks(1), "Fuga de agua en sala de servidores", "ASIGNADO", "INFRAESTRUCTURA"),
            new Ticket(7L, 1007L, "Remoto", "BAJA", LocalDateTime.now().minusMonths(1), "Cambio de contraseña", "CERRADO", "SOPORTE"),
            new Ticket(8L, 1008L, "Sede Central", "MEDIA", LocalDateTime.now().minusDays(3), "Instalación de software contable", "PENDIENTE", "REQUERIMIENTO"),
            new Ticket(9L, 1009L, "Sucursal Este", "CRITICA", LocalDateTime.now().minusHours(1), "Fallo de seguridad detectado", "EN_REVISION", "SEGURIDAD"),
            new Ticket(10L, 1010L, "Almacén", "BAJA", LocalDateTime.now().minusDays(20), "Limpieza de equipos antiguos", "ABIERTO", "MANTENIMIENTO")
    ));

    public List<Ticket> getTicketList(){
        return ticketList;
    }

    public Optional<Ticket> getTicketById(Long id){
        for (Ticket t: ticketList){
            if (t.getId().equals(id)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public Optional<Ticket> getTicketByTkNumber(Long tkNumber){
        for (Ticket t: ticketList){
            if (t.getTkNumber().equals(tkNumber)){
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public Ticket addTicket(Ticket t){
        ticketList.add(t);
        return t;
    }

    public boolean deleteTicket(Long id){
        Optional<Ticket> ticket = getTicketById(id);
        if (ticket.isEmpty()){
            return false;
        }
        return ticketList.remove(ticket.get());
    }

    public Ticket updateTicket(Ticket ticket){
        for (Ticket t: ticketList){
            if (t.getId().equals(ticket.getId())){
                t.setTkNumber(ticket.getTkNumber());
                t.setSite(ticket.getSite());
                t.setPriority(ticket.getPriority());
                t.setDescription(ticket.getDescription());
                t.setState(ticket.getState());
                t.setType(ticket.getType());
                return t;
            }
        }
        throw new ResourceNotFoundException("No se encontró en la DB el TK:" + ticket.getId());
    }


}
