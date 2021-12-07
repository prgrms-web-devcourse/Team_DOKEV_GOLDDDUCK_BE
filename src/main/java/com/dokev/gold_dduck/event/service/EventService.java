package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.common.exception.EntityNotFoundException;
import com.dokev.gold_dduck.common.exception.GiftEmptyException;
import com.dokev.gold_dduck.common.exception.MemberNotFoundException;
import com.dokev.gold_dduck.event.converter.EventFindConverter;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventSaveConverter eventSaveConverter;
    private final EventFindConverter eventFindConverter;
    private final MemberRepository memberRepository;

    public EventService(EventRepository eventRepository,
            EventSaveConverter eventConverter,
            EventFindConverter eventFindConverter,
            MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.eventSaveConverter = eventConverter;
        this.eventFindConverter = eventFindConverter;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public UUID saveEvent(EventSaveDto eventSaveRequest) {

        Member member = memberRepository.findById(eventSaveRequest.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(eventSaveRequest.getMemberId()));

        try {
            Event newEvent = eventSaveConverter.convertToEvent(eventSaveRequest, member);
            Event createdEvent = eventRepository.save(newEvent);
            return createdEvent.getCode();
        } catch (IllegalArgumentException e) {
            throw new GiftEmptyException(e.getMessage());
        }
    }

    public EventDto findDetailEventByCode(UUID eventCode) throws EntityNotFoundException {
        Event event = eventRepository.findGiftsByEventCode(eventCode)
                .orElseThrow(() -> new EntityNotFoundException(Event.class, eventCode));

        return eventFindConverter.convertToEventDto(event);
    }
}
