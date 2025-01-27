package com.pbdesafio.ms_ticket_manager.repositorys;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}
