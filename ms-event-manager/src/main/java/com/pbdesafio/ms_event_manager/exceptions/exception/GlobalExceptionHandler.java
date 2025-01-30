package com.pbdesafio.ms_event_manager.exceptions.exception;

import com.pbdesafio.ms_event_manager.exceptions.EventDeletionException;
import com.pbdesafio.ms_event_manager.exceptions.MissingFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<String> handleMissingFieldException(MissingFieldException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(EventDeletionException.class)
    public ResponseEntity<String> handleEventDeletionException(EventDeletionException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}