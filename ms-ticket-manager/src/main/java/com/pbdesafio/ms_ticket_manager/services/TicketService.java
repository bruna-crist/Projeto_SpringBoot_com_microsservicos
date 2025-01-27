package com.pbdesafio.ms_ticket_manager.services;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.EventDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.dtos.mapper.TicketMapper;
import com.pbdesafio.ms_ticket_manager.repositorys.EventClient;
import com.pbdesafio.ms_ticket_manager.repositorys.TicketRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventClient eventClient;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TicketService(TicketRepository ticketRepository, EventClient eventClient, RabbitTemplate rabbitTemplate) {
        this.ticketRepository = ticketRepository;
        this.eventClient = eventClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public TicketDTO createTicket(@NotNull Ticket ticket) {

        EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
        Ticket savedTicket = ticketRepository.save(ticket);

        rabbitTemplate.convertAndSend("ticket.exchange", "ticket.routingkey", savedTicket);
        return TicketMapper.toResponse(savedTicket, event);
    }

}
