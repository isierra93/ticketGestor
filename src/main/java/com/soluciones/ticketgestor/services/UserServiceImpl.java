package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()){
            return userOptional.get();
        }
        throw new ResourceNotFoundException("No se encontró el usuario con email: " + email);
    }
}
