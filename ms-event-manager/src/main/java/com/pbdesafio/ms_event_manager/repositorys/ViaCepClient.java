package com.pbdesafio.ms_event_manager.repositorys;

import com.pbdesafio.ms_event_manager.dtos.EventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    EventDTO getAddressByCep(@PathVariable("cep") String cep);
}
