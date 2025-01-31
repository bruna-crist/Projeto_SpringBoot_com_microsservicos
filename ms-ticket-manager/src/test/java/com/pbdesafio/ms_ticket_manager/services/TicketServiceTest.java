package com.pbdesafio.ms_ticket_manager.services;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.EventDTO;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.repositorys.EventClient;
import com.pbdesafio.ms_ticket_manager.repositorys.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventClient eventClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setCustomerName("Maria");
        ticket.setCpf("12345678901");
        ticket.setCustomerMail("maria@email.com");
        ticket.setEventId("event12");

        EventDTO eventDTO = new EventDTO();
        eventDTO.setId("event12");
        eventDTO.setEventName("Concerto");
        eventDTO.setDateTime("2024-12-31T20:00:00");

        when(eventClient.getEventById("event12")).thenReturn(ResponseEntity.ok(eventDTO));
        when(ticketRepository.save(any())).thenReturn(ticket);

        TicketDTO result = ticketService.createTicket(ticket);

        assertNotNull(result);
        assertEquals(ticket.getTicketId(), result.getTicketId());
        assertEquals(ticket.getCustomerName(), result.getCustomerName());
        assertEquals(ticket.getCpf(), result.getCpf());
        assertEquals(ticket.getCustomerMail(), result.getCustomerMail());
        assertEquals(ticket.getEventId(), result.getEvent().getId());
    }

    @Test
    void getTicketById() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findById("1")).thenReturn(Optional.of(ticket));
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        TicketDTO result = ticketService.getTicketById("1");

        assertEquals(ticket.getTicketId(), result.getTicketId());
    }

    @Test
    void getTicketsByCpf() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findByCpf("12345678901")).thenReturn(Collections.singletonList(ticket));
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        List<TicketDTO> result = ticketService.getTicketsByCpf("12345678901");

        assertEquals(1, result.size());
        assertEquals(ticket.getTicketId(), result.get(0).getTicketId());
    }

    @Test
    void updateTicket() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findById("1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        TicketDTO ticketDTO = new TicketDTO();
        TicketDTO result = ticketService.updateTicket("1", ticketDTO);

        assertEquals(ticket.getTicketId(), result.getTicketId());
    }

    @Test
    void cancelTicketById() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findById("1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any())).thenReturn(ticket);
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        TicketDTO result = ticketService.cancelTicketById("1");

        assertEquals("cancelado", result.getStatus());
    }

    @Test
    void cancelTicketsByCpf() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findByCpf("12345678901")).thenReturn(Collections.singletonList(ticket));
        when(ticketRepository.saveAll(any())).thenReturn(Collections.singletonList(ticket));
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        List<TicketDTO> result = ticketService.cancelTicketsByCpf("12345678901");

        assertEquals(1, result.size());
        assertEquals("cancelado", result.get(0).getStatus());
    }

    @Test
    void getTicketsByEventId() {
        Ticket ticket = new Ticket();
        EventDTO eventDTO = new EventDTO();
        when(ticketRepository.findByEventId("1")).thenReturn(Collections.singletonList(ticket));
        when(eventClient.getEventById(any())).thenReturn(ResponseEntity.ok(eventDTO));

        List<TicketDTO> result = ticketService.getTicketsByEventId("1");

        assertEquals(1, result.size());
        assertEquals(ticket.getTicketId(), result.get(0).getTicketId());
    }
}
