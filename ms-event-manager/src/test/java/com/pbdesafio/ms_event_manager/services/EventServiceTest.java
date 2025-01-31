package com.pbdesafio.ms_event_manager.services;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.dtos.TicketDTO;
import com.pbdesafio.ms_event_manager.exceptions.EventDeletionException;
import com.pbdesafio.ms_event_manager.exceptions.MissingFieldException;
import com.pbdesafio.ms_event_manager.repositorys.EventRepository;
import com.pbdesafio.ms_event_manager.repositorys.TicketClient;
import com.pbdesafio.ms_event_manager.repositorys.ViaCepClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private TicketClient ticketClient;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEvent() {
        Event event = new Event();
        event.setEventName("Test Event");
        event.setDateTime(LocalDateTime.now());
        event.setCep("12345678");

        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event createdEvent = eventService.createEvent(event);
        assertNotNull(createdEvent);
        assertEquals("Test Event", createdEvent.getEventName());
    }

    @Test
    public void testCreateEventWithValidCep() {
        Event event = new Event();
        event.setEventName("Test Event");
        event.setDateTime(LocalDateTime.now());
        event.setCep("12345678");

        EventDTO addressInfo = new EventDTO();
        addressInfo.setLogradouro("Rua Teste");
        addressInfo.setBairro("Bairro Teste");
        addressInfo.setLocalidade("Cidade Teste");
        addressInfo.setUf("TS");
        when(viaCepClient.getAddressByCep("12345678")).thenReturn(addressInfo);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);
        assertNotNull(createdEvent);
        assertEquals("Rua Teste", createdEvent.getLogradouro());
        assertEquals("Bairro Teste", createdEvent.getBairro());
        assertEquals("Cidade Teste", createdEvent.getLocalidade());
        assertEquals("TS", createdEvent.getUf());
    }

    @Test
    public void testGetEventById() {
        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));
        Event foundEvent = eventService.getEventById("1");
        assertNotNull(foundEvent);
        assertEquals("Test Event", foundEvent.getEventName());
    }

    @Test
    public void testGetAllEvents() {
        Event event = new Event();
        event.setId("1");
        event.setEventName("Test Event");
        when(eventRepository.findAll()).thenReturn(Collections.singletonList(event));
        List<Event> events = eventService.getAllEvents();
        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getEventName());
    }

    @Test
    public void testGetAllEventsSorted() {
        Event event1 = new Event();
        event1.setEventName("Event B");

        Event event2 = new Event();
        event2.setEventName("Event A");

        when(eventRepository.findAll()).thenReturn(List.of(event1, event2));
        List<Event> sortedEvents = eventService.getAllEventsSorted();
        assertEquals("Event A", sortedEvents.get(0).getEventName());
        assertEquals("Event B", sortedEvents.get(1).getEventName());
    }

    @Test
    public void testUpdateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("Updated Event");

        Event existingEvent = new Event();
        existingEvent.setId("1");
        existingEvent.setEventName("Test Event");
        when(eventRepository.findById("1")).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(existingEvent);
        Event updatedEvent = eventService.updateEvent("1", eventDTO);

        assertNotNull(updatedEvent);
        assertEquals("Updated Event", updatedEvent.getEventName());
    }

    @Test
    public void testDeleteEvent() {
        when(ticketClient.checkTicketsByEvent("1")).thenReturn(Collections.emptyList());
        eventService.deleteEvent("1");
    }
    @Test
    public void testCreateEventWithMissingFields() {
        Event event = new Event();
        event.setEventName("Test Event");

        MissingFieldException exception = assertThrows(MissingFieldException.class, () -> {
            eventService.createEvent(event);
        });

        assertEquals("O campo 'dateTime' é obrigatório.", exception.getMessage());
    }

    @Test
    public void testDeleteEventWithTickets() {
        when(ticketClient.checkTicketsByEvent("1")).thenReturn(List.of(new TicketDTO()));

        EventDeletionException exception = assertThrows(EventDeletionException.class, () -> {
            eventService.deleteEvent("1");
        });
        assertEquals("O evento não pode ser deletado porque possui ingressos vendidos.", exception.getMessage());
    }
}