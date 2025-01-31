package com.pbdesafio.ms_event_manager.exceptions;
import com.pbdesafio.ms_event_manager.exceptions.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    public void testHandleMissingFieldException() {
        String message = "The field 'eventName' is required.";
        MissingFieldException exception = new MissingFieldException(message);

        ResponseEntity<String> response = globalExceptionHandler.handleMissingFieldException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    public void testHandleEventDeletionException() {
        String message = "Event cannot be deleted because it has associated tickets.";
        EventDeletionException exception = new EventDeletionException(message);
        ResponseEntity<String> response = globalExceptionHandler.handleEventDeletionException(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}