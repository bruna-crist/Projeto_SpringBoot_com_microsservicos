package com.pbdesafio.ms_event_manager.services;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.dtos.TicketDTO;
import com.pbdesafio.ms_event_manager.exceptions.EventDeletionException;
import com.pbdesafio.ms_event_manager.exceptions.MissingFieldException;
import com.pbdesafio.ms_event_manager.repositorys.EventRepository;
import com.pbdesafio.ms_event_manager.repositorys.TicketClient;
import com.pbdesafio.ms_event_manager.repositorys.ViaCepClient;
import feign.FeignException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;
    private final TicketClient ticketClient;

    @Autowired
    public EventService(EventRepository eventRepository, ViaCepClient viaCepClient, TicketClient ticketClient) {
        this.eventRepository = eventRepository;
        this.viaCepClient = viaCepClient;
        this.ticketClient = ticketClient;
    }

    public Event createEvent(@NotNull Event event) {
        validateEvent(event);
            if (event.getCep() != null && !event.getCep().isEmpty()) {
                EventDTO addressInfo = viaCepClient.getAddressByCep(event.getCep());
                if (addressInfo != null) {
                    event.setLogradouro(addressInfo.getLogradouro());
                    event.setBairro(addressInfo.getBairro());
                    event.setLocalidade(addressInfo.getLocalidade());
                    event.setUf(addressInfo.getUf());
                }
            }
        return eventRepository.save(event);
    }
    private void validateEvent(Event event) {
        validateField(event.getEventName(), "eventName");
        validateField(event.getDateTime(), "dateTime");
        validateField(event.getCep(), "cep");
    }
    private void validateField(Object field, String fieldName) {
        if (field == null || (field instanceof String && ((String) field)
                .trim()
                .isEmpty())) {
            throw new MissingFieldException("O campo '" + fieldName + "' é obrigatório.");
        }
    }

    public Event getEventById(String id) {
        return eventRepository.findById(id)
                .orElse(null);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getAllEventsSorted() {
        return eventRepository.findAll()
                .stream()
                .sorted((event1, event2) -> event1
                        .getEventName()
                        .compareToIgnoreCase(event2.getEventName()))
                .collect(Collectors.toList());
    }

    public Event updateEvent(String id, EventDTO eventDTO) {
        Event existingEvent = eventRepository.findById(id).orElse(null);
        if (existingEvent == null) {
            return null;
        }
        if (eventDTO.getEventName() != null) {
            existingEvent.setEventName(eventDTO.getEventName());
        }
        if (eventDTO.getDateTime() != null) {
            existingEvent.setDateTime(eventDTO.getDateTime());
        }
        if (eventDTO.getCep() != null && !eventDTO.getCep().isEmpty()) {
            EventDTO addressInfo = viaCepClient.getAddressByCep(eventDTO.getCep());
            if (addressInfo != null) {
                existingEvent.setCep(eventDTO.getCep());
                existingEvent.setLogradouro(addressInfo.getLogradouro());
                existingEvent.setBairro(addressInfo.getBairro());
                existingEvent.setLocalidade(addressInfo.getLocalidade());
                existingEvent.setUf(addressInfo.getUf());
            }
        }
        return eventRepository.save(existingEvent);
    }
    public void deleteEvent(String eventId) {
        List<TicketDTO> tickets = Collections.emptyList();
        try {
            tickets = ticketClient.checkTicketsByEvent(eventId);
        } catch (FeignException.NotFound ignored) {
        }
        if (!tickets.isEmpty()) {
            throw new EventDeletionException("O evento não pode ser deletado porque possui ingressos vendidos.");
        }
        eventRepository.deleteById(eventId);
    }
}
