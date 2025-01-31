package com.pbdesafio.ms_ticket_manager.dtos.mapper;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.EventDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketMessageDTO;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;

public class TicketMapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static @NotNull TicketDTO toResponse(Ticket ticket, EventDTO event) {
        TicketDTO ticketDTO = mapper.map(ticket, TicketDTO.class);
        ticketDTO.setEvent(event);
        return ticketDTO;
    }
    public static TicketMessageDTO toMessage(Ticket ticket) {
        TicketMessageDTO messageDTO = mapper.map(ticket, TicketMessageDTO.class);
        return messageDTO;
    }
}
