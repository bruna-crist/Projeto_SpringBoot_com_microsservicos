package com.pbdesafio.ms_ticket_manager.repositorys;


import com.pbdesafio.ms_ticket_manager.dtos.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-event-manager", url = "http://ms-event-manager:8080")
public interface EventClient {

    @GetMapping("events/{id}")
    ResponseEntity<EventDTO> getEventById(@PathVariable String id);
}