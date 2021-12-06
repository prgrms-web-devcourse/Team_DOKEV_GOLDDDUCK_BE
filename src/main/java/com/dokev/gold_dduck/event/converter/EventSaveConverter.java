package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class EventSaveConverter {

    public Event convertToEvent(EventSaveDto eventSaveRequest, Member member) {
        return Event.builder(eventSaveRequest.getGiftChoiceType(),
                eventSaveRequest.getStartAt(),
                eventSaveRequest.getEndAt(),
                EventProgressStatus.RUNNING,
                eventSaveRequest.getMainTemplate(),
                eventSaveRequest.getMaxParticipantCount(),
                member)
            .code(UUID.randomUUID())
            .build();
    }

    public Gift convertToGift(GiftSaveDto giftSaveDto) {
        return new Gift(giftSaveDto.getCategory(), giftSaveDto.getGiftItems().size());
    }

    public GiftItem convertToGiftItem(GiftItemSaveDto giftItemSaveDto) {
        return new GiftItem(giftItemSaveDto.getGiftType(),
            giftItemSaveDto.getContent(),
            false);
    }

    public Event convertEventSaveDtoToEvent(EventSaveDto eventSaveRequest, Member member)
        throws IllegalArgumentException {
        Event newEvent = Event.builder(eventSaveRequest.getGiftChoiceType(),
                eventSaveRequest.getStartAt(),
                eventSaveRequest.getEndAt(),
                EventProgressStatus.RUNNING,
                eventSaveRequest.getMainTemplate(),
                eventSaveRequest.getMaxParticipantCount(),
                member)
            .code(UUID.randomUUID())
            .build();

        List<GiftSaveDto> GiftSaveDtos = eventSaveRequest.getGifts();
        Assert.notEmpty(GiftSaveDtos, "gifts must not be empty");

        GiftSaveDtos.forEach(giftSaveDto -> {
            Gift gift = convertGiftSaveDtoToGift(giftSaveDto);
            gift.changeEvent(newEvent);
        });
        return newEvent;
    }

    public Gift convertGiftSaveDtoToGift(GiftSaveDto giftSaveDto) throws IllegalArgumentException {
        Gift gift = new Gift(giftSaveDto.getCategory(), giftSaveDto.getGiftItems().size());
        List<GiftItemSaveDto> giftItemSaveDtos = giftSaveDto.getGiftItems();

        Assert.notEmpty(giftItemSaveDtos, "giftItems must not be empty");

        giftItemSaveDtos.forEach(giftItemSaveDto -> {
            GiftItem giftItem = convertGiftItemSaveDtoToGiftItem(giftItemSaveDto);
            giftItem.changeGift(gift);
        });

        return gift;
    }

    public GiftItem convertGiftItemSaveDtoToGiftItem(GiftItemSaveDto giftItemSaveDto) {
        return new GiftItem(giftItemSaveDto.getGiftType(),
            giftItemSaveDto.getContent(),
            false);
    }
}
