package com.soluciones.ticketgestor.exceptions;

import com.soluciones.ticketgestor.dtos.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Atrapa ResourceNotFoundException de los controladores
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e){

        ErrorDto errorDto = new ErrorDto(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(errorDto);
    }

    //Atrapa ResourceNotFoundException de los controladores
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException e){

        ErrorDto errorDto = new ErrorDto(
                e.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    //Atrapa ResourceIncompleteException de los controladores
    @ExceptionHandler(ResourceIncompleteException.class)
    public ResponseEntity<ErrorDto> handleResourceIncompleteException(ResourceIncompleteException e){
        ErrorDto errorDto = new ErrorDto(
                e.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    //Atrapa HttpMessageNotReadableException de la App
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){

        //Mensaje personalizado por Enum Priority
        if (e.getMessage().contains("Priority") && e.getMessage().contains("Enum")){
            ErrorDto errorDto = new ErrorDto(
                    "Error en la Prioridad del Ticket.",
                    "Error en el Enum Prioridad del Ticket.",
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
        }

        //Mensaje personalizado por Enum State
        if (e.getMessage().contains("State") && e.getMessage().contains("Enum")){
            ErrorDto errorDto = new ErrorDto(
                    "Error en el estado del Ticket.",
                    "Error en el Enum State del Ticket.",
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
        }

        //Mensaje genérico de error
        ErrorDto errorDto = new ErrorDto(
                "Error de formato en el JSON.",
                "Error de formato en el JSON." + HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}
