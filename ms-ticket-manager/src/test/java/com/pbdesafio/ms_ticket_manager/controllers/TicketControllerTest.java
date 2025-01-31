package com.pbdesafio.ms_ticket_manager.controllers;
import com.pbdesafio.ms_ticket_manager.controller.TicketController;
import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.services.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTicket() throws Exception {
        TicketDTO ticketDTO = new TicketDTO();
        when(ticketService.createTicket(any())).thenReturn(ticketDTO);
        ResponseEntity<TicketDTO> response = ticketController.createTicket(new Ticket());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ticketDTO, response.getBody());
    }

    @Test
    void getTicketById() {
        TicketDTO ticketDTO = new TicketDTO();
        when(ticketService.getTicketById("1")).thenReturn(ticketDTO);
        ResponseEntity<TicketDTO> response = ticketController.getTicketById("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTO, response.getBody());
    }

    @Test
    void getTicketsByCpf() {
        List<TicketDTO> tickets = Collections.singletonList(new TicketDTO());
        when(ticketService.getTicketsByCpf("12345678901")).thenReturn(tickets);
        ResponseEntity<List<TicketDTO>> response = ticketController.getTicketsByCpf("12345678901");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }

    @Test
    void updateTicketById() {
        TicketDTO ticketDTO = new TicketDTO();
        when(ticketService.updateTicket("1", ticketDTO)).thenReturn(ticketDTO);
        ResponseEntity<TicketDTO> response = ticketController.updateTicketById("1", ticketDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTO, response.getBody());
    }

    @Test
    void cancelTicketById() {
        when(ticketService.cancelTicketById("1")).thenReturn(new TicketDTO());
        ResponseEntity<TicketDTO> response = ticketController.cancelTicketById("1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void cancelTicketsByCpf() {
        List<TicketDTO> tickets = Collections.singletonList(new TicketDTO());
        when(ticketService.cancelTicketsByCpf("12345678901")).thenReturn(tickets);
        ResponseEntity<List<TicketDTO>> response = ticketController.cancelTicketsByCpf("12345678901");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }

    @Test
    void checkTicketsByEvent() {
        List<TicketDTO> tickets = Collections.singletonList(new TicketDTO());
        when(ticketService.getTicketsByEventId("1")).thenReturn(tickets);
        ResponseEntity<List<TicketDTO>> response = ticketController.checkTicketsByEvent("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tickets, response.getBody());
    }
}