package com.pbdesafio.ms_event_manager.repositorys;

import com.pbdesafio.ms_event_manager.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;


    public interface EventRepository extends MongoRepository<Event, String>{

    }
