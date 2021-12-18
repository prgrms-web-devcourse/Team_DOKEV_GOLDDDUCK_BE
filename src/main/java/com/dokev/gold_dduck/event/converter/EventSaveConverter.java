package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.common.util.TimeUtil;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class EventSaveConverter {

    public Event convertToEvent(EventSaveDto eventSaveRequest, Member member)
        throws IllegalArgumentException {
        int giftItemCount = eventSaveRequest.getGifts().stream()
            .mapToInt(giftSaveDto -> giftSaveDto.getGiftItems().size())
            .sum();

        LocalDateTime startAt = TimeUtil.seoulTimeToUtc(eventSaveRequest.getStartAt());
        LocalDateTime endAt = TimeUtil.seoulTimeToUtc(eventSaveRequest.getEndAt());

        Event newEvent = Event.builder(eventSaveRequest.getTitle(),
                eventSaveRequest.getGiftChoiceType(),
                startAt,
                endAt,
                EventProgressStatus.READY,
                eventSaveRequest.getMainTemplate(),
                eventSaveRequest.getMaxParticipantCount(),
                giftItemCount,
                eventSaveRequest.getMaxParticipantCount() - giftItemCount,
                member)
            .code(UUID.randomUUID())
            .build();

        List<GiftSaveDto> GiftSaveDtos = eventSaveRequest.getGifts();
        Assert.notEmpty(GiftSaveDtos, "gifts must not be empty");

        GiftSaveDtos.forEach(giftSaveDto -> {
            Gift gift = convertToGift(giftSaveDto);
            gift.changeEvent(newEvent);
        });
        return newEvent;
    }

    public Gift convertToGift(GiftSaveDto giftSaveDto) throws IllegalArgumentException {
        Gift gift = new Gift(giftSaveDto.getCategory(), giftSaveDto.getGiftItems().size());
        List<GiftItemSaveDto> giftItemSaveDtos = giftSaveDto.getGiftItems();

        Assert.notEmpty(giftItemSaveDtos, "giftItems must not be empty");

        giftItemSaveDtos.forEach(giftItemSaveDto -> {
            GiftItem giftItem = convertToGiftItem(giftItemSaveDto);
            giftItem.changeGift(gift);
        });

        return gift;
    }

    public GiftItem convertToGiftItem(GiftItemSaveDto giftItemSaveDto) {
        return new GiftItem(giftItemSaveDto.getGiftType(),
            giftItemSaveDto.getContent(),
            false);
    }

    public EventSaveDto convertToEventSaveDto(Event event) {
        return new EventSaveDto(event.getMember().getId(), event.getTitle(), convertToGiftSaveDtos(event.getGifts()),
            event.getGiftChoiceType(), event.getMainTemplate(), event.getStartAt(), event.getEndAt(),
            event.getMaxParticipantCount());
    }

    public List<GiftSaveDto> convertToGiftSaveDtos(List<Gift> gifts) {
        List<GiftSaveDto> giftSaveDtos = new ArrayList<>();
        gifts.forEach(gift -> {
            GiftSaveDto giftSaveDto = convertToGiftSaveDto(gift);
            giftSaveDtos.add(giftSaveDto);
        });
        return giftSaveDtos;
    }

    public GiftSaveDto convertToGiftSaveDto(Gift gift) {
        return new GiftSaveDto(gift.getCategory(), convertToGiftItemDtos(gift.getGiftItems()));
    }

    public List<GiftItemSaveDto> convertToGiftItemDtos(List<GiftItem> giftItems) {
        List<GiftItemSaveDto> giftItemSaveDtos = new ArrayList<>();
        giftItems.forEach(giftItem -> {
            GiftItemSaveDto giftItemSaveDto = convertToGiftItemDto(giftItem);
            giftItemSaveDtos.add(giftItemSaveDto);
        });
        return giftItemSaveDtos;
    }

    private GiftItemSaveDto convertToGiftItemDto(GiftItem giftItem) {
        return new GiftItemSaveDto(giftItem.getGiftType(), giftItem.getContent());
    }
}
