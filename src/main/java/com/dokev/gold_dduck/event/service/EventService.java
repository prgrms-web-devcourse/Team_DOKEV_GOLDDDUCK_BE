package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.event.converter.EventConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;

public class EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public EventService(EventRepository eventRepository,
            EventConverter eventConverter) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    public EventDto findDetailEventByCode(UUID eventCode) throws EntityNotFoundException {
        Optional<Event> event = eventRepository.findGiftsByEventCode(eventCode);
        if(event.isEmpty()){
            throw new EntityNotFoundException();
        }

        return eventConverter.convertToEventDto(event.get());
    }
}
