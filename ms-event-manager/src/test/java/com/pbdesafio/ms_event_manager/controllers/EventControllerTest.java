package com.pbdesafio.ms_event_manager.controllers;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.exceptions.EventDeletionException;
import com.pbdesafio.ms_event_manager.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("Test Event");
        eventDTO.setDateTime(LocalDateTime.now());
        eventDTO.setCep("12345678");

        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");

        when(eventService.createEvent(any(Event.class))).thenReturn(event);
        ResponseEntity<EventDTO> response = eventController.createEvent(eventDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Event", response.getBody().getEventName());
        assertEquals("/events/1", response.getHeaders().getFirst("Location"));
    }

    @Test
    public void testGetEventById() {
        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");
        when(eventService.getEventById("1")).thenReturn(event);
        ResponseEntity<EventDTO> response = eventController.getEventById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Event", response.getBody().getEventName());
    }

    @Test
    public void testGetEventByIdNotFound() {
        when(eventService.getEventById("1")).thenReturn(null);
        ResponseEntity<EventDTO> response = eventController.getEventById("1");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetAllEvents() {
        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");

        when(eventService.getAllEvents()).thenReturn(Collections.singletonList(event));
        ResponseEntity<List<EventDTO>> response = eventController.getAllEvents();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Event", response.getBody().get(0).getEventName());
    }

    @Test
    public void testGetAllEventsSorted() {
        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");

        when(eventService.getAllEventsSorted()).thenReturn(Collections.singletonList(event));

        ResponseEntity<List<EventDTO>> response = eventController.getAllEventsSorted();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Event", response.getBody().get(0).getEventName());
    }

    @Test
    public void testUpdateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("Updated Event");

        Event updatedEvent = new Event();
        updatedEvent.setId("1");
        updatedEvent.setEventName("Updated Event");
        when(eventService.updateEvent("1", eventDTO)).thenReturn(updatedEvent);
        ResponseEntity<EventDTO> response = eventController.updateEvent("1", eventDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Event", response.getBody().getEventName());
    }

    @Test
    public void testUpdateEventNotFound() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("Updated Event");

        when(eventService.updateEvent("1", eventDTO)).thenReturn(null);
        ResponseEntity<EventDTO> response = eventController.updateEvent("1", eventDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteEvent() {
        ResponseEntity<String> response = eventController.deleteEvent("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public void testDeleteEventWithTickets() {
        doThrow(new EventDeletionException("Event has tickets"))
                .when(eventService)
                .deleteEvent("1");

        ResponseEntity<String> response = eventController.deleteEvent("1");
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Event has tickets", response.getBody());
    }
}