package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.common.ApiResponse;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventConverter;
import com.dokev.gold_dduck.event.dto.EventResponse;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.gift.domain.Gift;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public EventService(EventRepository eventRepository, EventConverter eventConverter) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    public ApiResponse<EventResponse> findDetailEvent(UUID code) {
        Event event = eventRepository.findEventByCode(code);
        List<Gift> gifts = eventRepository.findGiftsByEventId(event.getId());

        for (Gift gift : gifts) {
            gift.changeEvent(event);
        }

        return eventConverter.convertToApiResponse(event);
    }

}
