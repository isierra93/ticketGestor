package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.CommentDto;
import com.soluciones.ticketgestor.dtos.SaveCommentDto;
import com.soluciones.ticketgestor.dtos.UserOwnerDto;
import com.soluciones.ticketgestor.models.Comment;
import com.soluciones.ticketgestor.models.Ticket;
import com.soluciones.ticketgestor.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(@Lazy UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setComment(comment.getComment());

        UserOwnerDto userOwnerDto = userMapper.toDto(comment.getUser());
        dto.setUserOwnerDto(userOwnerDto);

        return dto;
    }

    public Comment toEntity(SaveCommentDto saveDto, User user, Ticket ticket) {
        Comment comment = new Comment();
        comment.setComment(saveDto.getComment());
        comment.setUser(user);
        comment.setTicket(ticket);
        return comment;
    }

}
