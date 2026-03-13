package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.dtos.UserDto;
import com.soluciones.ticketgestor.exceptions.InvalidDataFormatException;
import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.exceptions.ResourceIncompleteException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.mappers.UserMapper;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.models.UserRole;
import com.soluciones.ticketgestor.repositories.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            return userOptional.get();
        }
        throw new ResourceNotFoundException("No se encontró el usuario con email: " + email);
    }

    public Boolean isEmailDisposable(String email){
        return !userRepository.existsByEmail(email);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User convertToEntity(UserDto userDto){
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.ADMIN);
        return user;
    }

    public void validateNullsAndEmailDisposable(UserDto userDto){
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()){
            throw new ResourceIncompleteException("Campo email incompleto.");
        }
        if (userDto.getEmail().contains(" ")){
            throw new InvalidDataFormatException("El email no puede contener espacios.");
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()){
            throw new ResourceIncompleteException("Campo password incompleto.");
        }
        if (userDto.getPassword().contains(" ")){
            throw new InvalidDataFormatException("La contraseña no puede contener espacios.");
        }
        if (!this.isEmailDisposable(userDto.getEmail())){
            throw new ResourceAlreadyExistsException("El email: " + userDto.getEmail() + " ya se encuentra en uso.");
        }
    }
}
