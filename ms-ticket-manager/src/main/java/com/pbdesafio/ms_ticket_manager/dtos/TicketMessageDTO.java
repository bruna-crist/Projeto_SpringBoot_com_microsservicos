package com.pbdesafio.ms_ticket_manager.dtos;

import lombok.Data;

@Data
public class TicketMessageDTO {
    private String ticketId;
    private String cpf;
    private String customerName;
    private String customerMail;
    private EventDTO event;
    private String brlAmount;
    private String usdAmount;
    private String status;
}
