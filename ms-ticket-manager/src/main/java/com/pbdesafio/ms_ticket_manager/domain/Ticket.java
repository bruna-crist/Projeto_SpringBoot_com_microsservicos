package com.pbdesafio.ms_ticket_manager.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Document(collection = "tickets")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String ticketId;

    @Field ("customer_name")
    private String customerName;
    @Field ("cpf")
    private String cpf;
    @Field ("customer_mail")
    private String customerMail;
    @Field ("event_id")
    private String eventId;
    @Field ("event_name")
    private String eventName;
    @Field ("brl_amount")
    private String BRLamount;
    @Field ("usd_amount")
    private String USDamount;
    @Field ("status")
    private String status = "concluído";
}
