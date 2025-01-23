package com.pbdesafio.ms_event_manager.services;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import com.pbdesafio.ms_event_manager.repositorys.EventRepository;
import com.pbdesafio.ms_event_manager.repositorys.ViaCepClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final ViaCepClient viaCepClient;

    @Autowired
    public EventService(EventRepository eventRepository, ViaCepClient viaCepClient) {
        this.eventRepository = eventRepository;
        this.viaCepClient = viaCepClient;
    }

    public Event createEvent(Event event) {
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

    public Event getEventById(String id) {
        return eventRepository.findById(id).orElse(null);
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


}
