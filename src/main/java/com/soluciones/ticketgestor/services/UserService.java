package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.dtos.UserDto;
import com.soluciones.ticketgestor.models.User;

public interface UserService {
    User getUserByEmail(String email);
    Boolean isEmailDisposable(String email);
    User saveUser(User user);
    User convertToEntity(UserDto userDto);
    void validateNullsAndEmailDisposable(UserDto userDto);
}
