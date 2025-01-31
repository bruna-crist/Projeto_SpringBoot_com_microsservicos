package com.pbdesafio.ms_event_manager.dtos;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.mapper.EventMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventMapperTest {

    @Test
    public void testToEntity() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setEventName("Test Event");
        eventDTO.setDateTime(LocalDateTime.now());
        eventDTO.setCep("12345678");

        Event event = EventMapper.toEntity(eventDTO);
        assertEquals(eventDTO.getEventName(), event.getEventName());
        assertEquals(eventDTO.getDateTime(), event.getDateTime());
        assertEquals(eventDTO.getCep(), event.getCep());
    }
    @Test
    public void testToDto() {
        Event event = new Event();
        event.setEventName("Test Event");
        event.setDateTime(LocalDateTime.now());
        event.setCep("12345678");

        EventDTO eventDTO = EventMapper.toDto(event);
        assertEquals(event.getEventName(), eventDTO.getEventName());
        assertEquals(event.getDateTime(), eventDTO.getDateTime());
        assertEquals(event.getCep(), eventDTO.getCep());
    }
}
