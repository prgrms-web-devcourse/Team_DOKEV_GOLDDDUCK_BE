package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.common.exception.EntityNotFoundException;
import com.dokev.gold_dduck.common.exception.GiftEmptyException;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
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
    private final EventSaveConverter eventConverter;
    private final MemberRepository memberRepository;

    public EventService(EventRepository eventRepository,
        EventSaveConverter eventConverter, MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public UUID saveEvent(EventSaveDto eventSaveRequest) {

        Member member = memberRepository.findById(eventSaveRequest.getMemberId())
            .orElseThrow(() -> new EntityNotFoundException(Member.class, eventSaveRequest.getMemberId()));

        try {
            Event newEvent = eventConverter.convertToEvent(eventSaveRequest, member);
            Event createdEvent = eventRepository.save(newEvent);
            return createdEvent.getCode();
        } catch (IllegalArgumentException e) {
            throw new GiftEmptyException(e.getMessage());
        }
    }
}
