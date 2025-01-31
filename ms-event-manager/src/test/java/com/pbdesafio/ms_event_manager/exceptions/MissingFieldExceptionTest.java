package com.pbdesafio.ms_event_manager.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MissingFieldExceptionTest {

    @Test
    public void testMissingFieldException() {
        String message = "The field 'eventName' is required.";
        MissingFieldException exception = new MissingFieldException(message);
        assertEquals(message, exception.getMessage());
    }
}