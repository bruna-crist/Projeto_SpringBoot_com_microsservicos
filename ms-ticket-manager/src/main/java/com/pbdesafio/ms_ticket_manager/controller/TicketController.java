package com.pbdesafio.ms_ticket_manager.controller;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import com.pbdesafio.ms_ticket_manager.dtos.TicketDTO;
import com.pbdesafio.ms_ticket_manager.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/create-ticket")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody Ticket ticket) {
        TicketDTO ticketResponse = ticketService.createTicket(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketResponse);
    }
    @GetMapping("/get-ticket/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable String id) {
        TicketDTO ticketResponse = ticketService.getTicketById(id);
        if (ticketResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(ticketResponse);
    }
    @GetMapping("/get-ticket-by-cpf/{cpf}")
    public ResponseEntity<List<TicketDTO>> getTicketsByCpf(@PathVariable String cpf) {
        List<TicketDTO> ticketResponses = ticketService.getTicketsByCpf(cpf);
        if (ticketResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(ticketResponses);
    }

    @PutMapping("/update-ticket/{id}")
    public ResponseEntity<TicketDTO> updateTicketById(@PathVariable String id, @RequestBody TicketDTO ticketDTO) {
        TicketDTO updatedTicket = ticketService.updateTicket(id, ticketDTO);
        if (updatedTicket == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updatedTicket);
    }
}

