package com.pbdesafio.ms_event_manager.repositorys;

import com.pbdesafio.ms_event_manager.domain.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testSaveAndFindById() {
        Event event = new Event();
        event.setEventName("Test Event");
        event.setDateTime(LocalDateTime.now());
        event.setCep("12345678");

        Event savedEvent = eventRepository.save(event);
        Optional<Event> foundEvent = eventRepository.findById(savedEvent.getId());
        assertTrue(foundEvent.isPresent());
        assertEquals("Test Event", foundEvent.get().getEventName());
    }
}