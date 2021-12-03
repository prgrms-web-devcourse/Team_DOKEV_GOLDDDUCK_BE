package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.common.error.ErrorCode;
import com.dokev.gold_dduck.common.exception.BusinessException;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventConverter;
import com.dokev.gold_dduck.event.dto.EventResponseDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.gift.converter.GiftConverter;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.dto.GiftResponseDto;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.converter.MemberConverter;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberResponseDto;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final EventConverter eventConverter;
    private final GiftConverter giftConverter;
    private final MemberConverter memberConverter;

    public EventService(EventRepository eventRepository, GiftRepository giftRepository,
            MemberRepository memberRepository,
            EventConverter eventConverter, GiftConverter giftConverter, MemberConverter memberConverter) {
        this.eventRepository = eventRepository;
        this.memberRepository = memberRepository;
        this.eventConverter = eventConverter;
        this.giftConverter = giftConverter;
        this.memberConverter = memberConverter;
    }

    public EventResponseDto findDetailEvent(UUID code) {
        Optional<Event> event = eventRepository.findEventByCode(code);
        if (event.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }
        Member member = memberRepository.findMemberByEventId(event.get().getId());
        List<Gift> gifts = eventRepository.findGiftsByEventId(event.get().getId());

        List<GiftResponseDto> giftResponseDtos = gifts.stream()
                .map(giftConverter::convertToGiftResponseDto)
                .collect(Collectors.toList());

        MemberResponseDto memberResponseDto = memberConverter.convertToMemberResponseDto(member);

        return eventConverter.convertToEventResponseDto(event.get(), giftResponseDtos, memberResponseDto);
    }

}
