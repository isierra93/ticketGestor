package com.soluciones.ticketgestor.mappers;

import com.soluciones.ticketgestor.dtos.UserDto;
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

    public User toEntity(UserDto userDto){
        User userEntity = new User();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        return userEntity;
    }

}
