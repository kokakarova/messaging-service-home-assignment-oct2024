package com.karova.messaging_service.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({EntityNotFoundException.class})
    private ResponseEntity<CustomError> deliveryUnavailableException(RuntimeException exception) {
        CustomError customError = new CustomError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customError);
    }
}
