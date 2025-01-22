package com.pbdesafio.ms_event_manager.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventDTO {

    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String uf;
}
