package com.pbdesafio.ms_event_manager.dtos.mapper;

import com.pbdesafio.ms_event_manager.domain.Event;
import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import org.modelmapper.ModelMapper;

public class EventMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static Event toEntity(EventDTO eventDTO) {
        return mapper.map(eventDTO, Event.class);
    }

    public static EventDTO toDto(Event event) {
        return mapper.map(event, EventDTO.class);
    }
}
