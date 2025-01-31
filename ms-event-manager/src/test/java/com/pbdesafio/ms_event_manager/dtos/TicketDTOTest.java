package com.pbdesafio.ms_event_manager.dtos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketDTOTest {

    @Test
    public void testTicketDTO() {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId("1");
        ticketDTO.setCpf("12345678901");
        ticketDTO.setEventId("1");
        assertEquals("1", ticketDTO.getTicketId());
        assertEquals("12345678901", ticketDTO.getCpf());
        assertEquals("1", ticketDTO.getEventId());
    }
}
