package com.pbdesafio.ms_event_manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
@JsonIgnoreProperties
@Data
@Document(collection = "events")
public class Event {

    @Id
    private String id;

    @Field("event_name")
    private String eventName;

    @Field("date_time")
    private LocalDateTime dateTime;

    @Field("cep")
    private String cep;

    @Field("logradouro")
    private String logradouro;

    @Field("bairro")
    private String bairro;

    @Field("cidade")
    private String cidade;

    @Field("uf")
    private String uf;

}
