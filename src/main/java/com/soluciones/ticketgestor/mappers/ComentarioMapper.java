package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.ComentarioDto;
import com.soluciones.ticketgestor.dtos.SaveComentarioDto;
import com.soluciones.ticketgestor.dtos.UserOwnerDto;
import com.soluciones.ticketgestor.models.Comentario;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ComentarioMapper {

    private final UserMapper userMapper;

    public ComentarioMapper(@Lazy UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ComentarioDto toDto(Comentario comentario) {
        ComentarioDto dto = new ComentarioDto();
        dto.setId(comentario.getId());
        dto.setCreatedAt(comentario.getCreatedAt());
        dto.setComentario(comentario.getComentario());

        UserOwnerDto userOwnerDto = userMapper.toDto(comentario.getUser());
        dto.setUserOwnerDto(userOwnerDto);

        return dto;
    }

    public Comentario toEntity(SaveComentarioDto saveDto, User user, Ticket ticket) {
        Comentario comentario = new Comentario();
        comentario.setComentario(saveDto.getComentario());
        comentario.setUser(user);
        comentario.setTicket(ticket);
        return comentario;
    }

}
