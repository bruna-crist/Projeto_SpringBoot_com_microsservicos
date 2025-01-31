package com.pbdesafio.ms_ticket_manager.dtos;

import lombok.Data;

@Data
public class TicketDTO {
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventDTO event;
    private String BRLamount;
    private String USDamount;
    private String status;
}

