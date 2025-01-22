package com.pbdesafio.ms_event_manager.controllers;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.dtos.mapper.EventMapper;
import com.pbdesafio.ms_event_manager.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create-event")
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = EventMapper.toEntity(eventDTO);
        Event createdEvent = eventService.createEvent(event);
        EventDTO createdEventDTO = EventMapper.toDto(createdEvent);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/events/" + createdEvent.getId())
                .body(createdEventDTO);
    }
}

