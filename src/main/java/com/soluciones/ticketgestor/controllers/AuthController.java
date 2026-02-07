package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.UserDto;
import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.exceptions.ResourceNotFoundException;
import com.soluciones.ticketgestor.models.Role;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.repositories.UserRepository;
import com.soluciones.ticketgestor.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> postRegister(@RequestBody UserDto userDto){
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new ResourceAlreadyExistsException("El email ya se encuentra en uso.");
        }

        User userEntity = new User();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setRole(Role.USER);

        userRepository.save(userEntity);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> postLogin(@RequestBody UserDto userDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (userDto.getEmail(), userDto.getPassword())
        );

        Optional<User> userEntity = userRepository.findByEmail(userDto.getEmail());
        if (userEntity.isPresent()){
            String token = jwtUtils.generateToken(userEntity.get());
            return ResponseEntity.ok(token);
        }

        throw new ResourceNotFoundException("No se encontró el usuario.");
    }
}
