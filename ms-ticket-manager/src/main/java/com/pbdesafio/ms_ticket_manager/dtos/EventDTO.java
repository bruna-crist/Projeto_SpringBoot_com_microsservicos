package com.pbdesafio.ms_ticket_manager.dtos;

import lombok.Data;

@Data
public class EventDTO {
    private String id;
    private String eventName;
    private String dateTime;
    private String logradouro;
    private String bairro;
    private String localidade;
    private String uf;
}
