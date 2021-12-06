package com.dokev.gold_dduck.event.converter;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.member.domain.Member;
import java.util.UUID;
import org.springframework.stereotype.Component;

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
}
