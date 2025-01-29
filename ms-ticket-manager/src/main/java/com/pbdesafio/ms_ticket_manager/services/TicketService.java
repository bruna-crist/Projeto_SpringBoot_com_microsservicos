package com.pbdesafio.ms_ticket_manager.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.EventDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketMessageDTO;
import com.pbdesafio.ms_ticket_manager.dtos.mapper.TicketMapper;
import com.pbdesafio.ms_ticket_manager.exceptions.MissingFieldException;
import com.pbdesafio.ms_ticket_manager.repositorys.EventClient;
import com.pbdesafio.ms_ticket_manager.repositorys.TicketRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public TicketDTO createTicket(@NotNull Ticket ticket)
            throws JsonProcessingException {
            validateTicket(ticket);

            EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
            Ticket savedTicket = ticketRepository.save(ticket);
            TicketMessageDTO messageDTO = TicketMapper.toMessage(savedTicket);
            messageDTO.setEvent(event);

            rabbitTemplate.convertAndSend("ticket.exchange", "ticket.routingkey", messageDTO);
            ObjectMapper mapper = new ObjectMapper();
            String formattedJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageDTO);
            System.out.println("Seu ingresso foi criado com sucesso: \n" + formattedJson
                    + "\n Aproveite o Evento! :)");
            return TicketMapper.toResponse(savedTicket, event);
    }

    private void validateTicket(@NotNull Ticket ticket) {
        Map<String, String> fields = new HashMap<>();
        fields.put("customerName", ticket.getCustomerName());
        fields.put("cpf", ticket.getCpf());
        fields.put("customerMail", ticket.getCustomerMail());
        fields.put("eventId", ticket.getEventId());

        fields.forEach((field, value) -> {
            if (value == null || value.trim().isEmpty()) {
                throw new MissingFieldException("O campo '" + field + "' é obrigatório.");
            }
        });
    }

    public TicketDTO getTicketById(String id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            return null;
        }
        EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
        return TicketMapper.toResponse(ticket, event);
    }

    public List<TicketDTO> getTicketsByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        return tickets.stream()
                .map(ticket -> {
                    EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
                    return TicketMapper.toResponse(ticket, event);
                })
                .collect(Collectors.toList());
    }

    public TicketDTO updateTicket(String id, TicketDTO ticketDTO) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if (ticketOptional.isEmpty()) {
            return null;
        }
        Ticket ticket = ticketOptional.get();
        ticket.setCpf(ticketDTO.getCpf());
        ticket.setCustomerName(ticketDTO.getCustomerName());
        ticket.setCustomerMail(ticketDTO.getCustomerMail());

        Ticket updatedTicket = ticketRepository.save(ticket);
        EventDTO event = eventClient.getEventById(updatedTicket.getEventId()).getBody();

        return TicketMapper.toResponse(updatedTicket, event);
    }

    public TicketDTO cancelTicketById(String id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            return null;
        }
        Ticket ticket = optionalTicket.get();
        ticket.setStatus("cancelado");
        Ticket updatedTicket = ticketRepository.save(ticket);
        EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
        return TicketMapper.toResponse(updatedTicket,event);
    }

    public List<TicketDTO> cancelTicketsByCpf(String cpf) {
        List<Ticket> tickets = ticketRepository.findByCpf(cpf);
        if (tickets.isEmpty()) {
        return null;
    }
        tickets.forEach(ticket -> ticket.setStatus("cancelado"));
        List<Ticket> updatedTickets = ticketRepository.saveAll(tickets);
        return updatedTickets.stream()
            .map(ticket -> {
                EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
                return TicketMapper.toResponse(ticket, event);})
            .toList();

    }

    public List<TicketDTO> getTicketsByEventId(String eventId) {
        List<Ticket> tickets = ticketRepository.findByEventId(eventId);
        return tickets.stream()
                .map(ticket -> {
                    EventDTO event = eventClient.getEventById(ticket.getEventId()).getBody();
                    return TicketMapper.toResponse(ticket, event);})
                .toList();
    }

}
