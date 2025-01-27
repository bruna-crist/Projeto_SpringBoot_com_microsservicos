package com.pbdesafio.ms_ticket_manager.repositorys;

import com.pbdesafio.ms_ticket_manager.domain.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByCpf(String cpf);
}
