package com.pbdesafio.ms_event_manager.dtos;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventDTOTest {

    @Test
    public void testEventDTO() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId("1");
        eventDTO.setEventName("Test Event");
        eventDTO.setDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        eventDTO.setCep("12345678");
        eventDTO.setLogradouro("Rua Teste");
        eventDTO.setBairro("Bairro Teste");
        eventDTO.setLocalidade("Cidade Teste");
        eventDTO.setUf("TS");

        assertEquals("1", eventDTO.getId());
        assertEquals("Test Event", eventDTO.getEventName());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), eventDTO.getDateTime());
        assertEquals("12345678", eventDTO.getCep());
        assertEquals("Rua Teste", eventDTO.getLogradouro());
        assertEquals("Bairro Teste", eventDTO.getBairro());
        assertEquals("Cidade Teste", eventDTO.getLocalidade());
        assertEquals("TS", eventDTO.getUf());
    }
}
