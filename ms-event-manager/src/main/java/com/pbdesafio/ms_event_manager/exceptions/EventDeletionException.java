package com.pbdesafio.ms_event_manager.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EventDeletionException extends RuntimeException {
    public EventDeletionException(String message) {
        super(message);
    }
}
