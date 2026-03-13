package com.soluciones.ticketgestor.controllers;

import com.soluciones.ticketgestor.dtos.ErrorDto;
import com.soluciones.ticketgestor.dtos.TokenDto;
import com.soluciones.ticketgestor.dtos.UserDto;
import com.soluciones.ticketgestor.models.User;
import com.soluciones.ticketgestor.security.JwtUtils;
import com.soluciones.ticketgestor.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Tag(
        name = "Autenticación",
        description = "Endpoints para el registro de nuevos usuarios y la obtención de tokens JWT."
)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }


    @Operation(
            summary = "Registrar un nuevo usuario.",
            description = "Registra un usuario en la base de datos."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Operación exitosa. Usuario creado correctamente."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Operación fallida. Los campos email y password son incorrectos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = {
                                            @ExampleObject(
                                            name = "Email/Password incompleto.",
                                            value = """
                                                    {
                                                      "message": "Campo email/password incompleto.",
                                                      "error": "Bad Request",
                                                      "status": 400,
                                                      "timestamp": "2026-03-11T03:03:05.5591445"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Email/Contraseña con espacios.",
                                                    value = """
                                                    {
                                                      "message": "El email/contraseña no puede contener espacios.",
                                                      "error": "Bad Request",
                                                      "status": 400,
                                                      "timestamp": "2026-03-11T03:03:05.5591445"
                                                    }
                                                    """
                                            ),
                                            @ExampleObject(
                                                    name = "Formato de JSON inválido.",
                                                    value = """
                                                        {
                                                          "message": "Error de formato en el JSON.",
                                                          "error": "Bad Request",
                                                          "status": 400,
                                                          "timestamp": "2026-03-12T08:20:50.6185708"
                                                        }
                                                    """
                                            )

                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Operación fallida. El email indicado se encuentra en uso.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDto.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "El email ya se encuentra en uso.",
                                                      "error": "Conflict",
                                                      "status": 409,
                                                      "timestamp": "2026-03-11T01:34:24.4297731"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Void> postRegister(@RequestBody UserDto userDto){
        userService.validateNullsAndEmailDisposable(userDto);
        User user = userService.convertToEntity(userDto);
        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Iniciar sesión con un usuario.",
            description = "Inicia sesión con un usuario previamente registrado, brindándole un token JWT."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión correcto. Recibe un Token JWT.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema (implementation = TokenDto.class),
                                    examples =
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhMiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzczMzE1NDIwLCJleHAiOjE3NzM0MDE4MjB9.hFzdWNZ6HN7qfZ3FPyxq8DzrssowQNdovUULBq43-30"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Inicio de sesión fallido. El formato de JSON es incorrecto.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples =
                                    @ExampleObject(
                                            value = """
                                                        {
                                                          "message": "Error de formato en el JSON.",
                                                          "error": "Bad Request.",
                                                          "status": 400,
                                                          "timestamp": "2026-03-12T08:20:50.6185708"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Inicio de sesión fallído. Email/Password incorrectos.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples =
                                    @ExampleObject(
                                            value = """
                                                    {
                                                      "message": "Email o contraseña incorrectos.",
                                                      "error": "Unauthorized",
                                                      "status": 401,
                                                      "timestamp": "2026-03-12T08:16:00.9614938"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDto> postLogin(@RequestBody UserDto userDto){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken (userDto.getEmail(), userDto.getPassword())
        );
        TokenDto tokenDto = new TokenDto(jwtUtils.generateToken((UserDetails) auth.getPrincipal()));
        return ResponseEntity.ok(tokenDto);
    }
}
