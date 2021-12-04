package com.dokev.gold_dduck.event.service;

import com.dokev.gold_dduck.common.exception.GiftEmptyException;
import com.dokev.gold_dduck.common.exception.MemberNotFoundException;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventSaveRequest;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.repository.GiftItemRepository;
import com.dokev.gold_dduck.gift.repository.GiftRepository;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final GiftRepository giftRepository;
    private final GiftItemRepository giftItemRepository;
    private final EventSaveConverter eventConverter;
    private final MemberRepository memberRepository;

    public EventService(EventRepository eventRepository, GiftRepository giftRepository,
        GiftItemRepository giftItemRepository, EventSaveConverter eventConverter,
        MemberRepository memberRepository) {
        this.eventRepository = eventRepository;
        this.giftRepository = giftRepository;
        this.giftItemRepository = giftItemRepository;
        this.eventConverter = eventConverter;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public UUID saveEvent(EventSaveRequest eventSaveRequest) {

        Member member = memberRepository.findById(eventSaveRequest.getMemberId())
            .orElseThrow(() -> new MemberNotFoundException(eventSaveRequest.getMemberId()));

        Event event = eventConverter.converterToEvent(eventSaveRequest, member);

        List<GiftSaveDto> giftSaveDtos = eventSaveRequest.getGifts();

        List<Gift> gifts = new ArrayList<>();
        List<GiftItem> giftItems = new ArrayList<>();

        giftSaveDtos.forEach(giftSaveDto -> {
            Gift gift = eventConverter.converterToGift(giftSaveDto);
            gift.changeEvent(event);
            gifts.add(gift);
            giftSaveDto.getGiftItems()
                .forEach(giftItemSaveDto -> {
                    GiftItem giftItem = eventConverter.converterToGiftItem(giftItemSaveDto);
                    giftItem.changeGift(gift);
                    giftItems.add(giftItem);
                });
        });

        try {
            Assert.notEmpty(giftItems, "giftItems must not be empty");
            Assert.notEmpty(gifts, "gifts must not be empty");
        } catch (IllegalArgumentException e) {
            throw new GiftEmptyException(e.getMessage());
        }

        Event createdEvent = eventRepository.save(event);
        giftRepository.saveAll(gifts);
        giftItemRepository.saveAll(giftItems);

        return createdEvent.getCode();
    }
}
