package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.UserOwnerDto;
import com.soluciones.ticketgestor.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserOwnerDto toDto(User user) {
        UserOwnerDto userOwnerDto = new UserOwnerDto();
        userOwnerDto.setEmail(user.getEmail());
        return userOwnerDto;
    }

}
