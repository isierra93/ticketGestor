package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.UserDto;
import com.soluciones.ticketgestor.exceptions.ResourceAlreadyExistsException;
import com.soluciones.ticketgestor.models.UserRole;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.repositories.UserRepository;
import com.soluciones.ticketgestor.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para el registro de nuevos usuarios y la obtención de tokens JWT.")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Registrar un nuevo usuario.",
            description = "Registra un usuario en la base de datos."
    )
    @PostMapping("/register")
    public ResponseEntity<?> postRegister(@RequestBody UserDto userDto){
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new ResourceAlreadyExistsException("El email: " + userDto.getEmail() + " ya se encuentra en uso.");
        }

        User userEntity = new User();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setRole(UserRole.ADMIN);

        userRepository.save(userEntity);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Iniciar sesión con un usuario.",
            description = "Inicia sesión con un usuario previamente registrado, brindándole un token JWT."
    )
    @PostMapping("/login")
    public ResponseEntity<?> postLogin(@RequestBody UserDto userDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (userDto.getEmail(), userDto.getPassword())
        );

        System.out.println(auth.getPrincipal());
        String token = jwtUtils.generateToken((UserDetails) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }
}
