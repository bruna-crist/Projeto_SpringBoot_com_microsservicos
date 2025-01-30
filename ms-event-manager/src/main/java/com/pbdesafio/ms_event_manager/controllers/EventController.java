package com.pbdesafio.ms_event_manager.controllers;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.dtos.mapper.EventMapper;
import com.pbdesafio.ms_event_manager.exceptions.EventDeletionException;
import com.pbdesafio.ms_event_manager.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

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

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable String id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(EventMapper.toDto(event));
    }
    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventDTO> eventDTOs = events.stream()
                .map(EventMapper::toDto)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }
    @GetMapping("/get-all-events/sorted")
    public ResponseEntity<List<EventDTO>> getAllEventsSorted() {
        List<Event> events = eventService.getAllEventsSorted();
        List<EventDTO> eventDTOs = events.stream()
                .map(EventMapper::toDto)
                .toList();
        return ResponseEntity.ok(eventDTOs);
    }

    @PutMapping("/update-event/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable String id, @Valid @RequestBody EventDTO eventDTO) {
        Event updatedEvent = eventService.updateEvent(id, eventDTO);
        if (updatedEvent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(EventMapper.toDto(updatedEvent));
    }
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable String id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (EventDeletionException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}

