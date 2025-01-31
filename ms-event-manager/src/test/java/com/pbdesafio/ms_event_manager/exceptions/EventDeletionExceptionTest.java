package com.pbdesafio.ms_event_manager.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDeletionExceptionTest {

    @Test
    public void testEventDeletionException() {
        String message = "Event cannot be deleted because it has associated tickets.";
        EventDeletionException exception = new EventDeletionException(message);
        assertEquals(message, exception.getMessage());
    }
}