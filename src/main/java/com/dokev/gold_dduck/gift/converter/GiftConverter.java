package com.dokev.gold_dduck.gift.converter;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDetailDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import com.dokev.gold_dduck.gift.dto.GiftItemSearchDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GiftConverter {

    public GiftDto convertToGiftDto(Gift gift) {
        List<GiftItem> giftItems = gift.getGiftItems();
        List<GiftItemDto> giftItemDtos = giftItems.stream()
            .map(this::convertToGiftItemDto)
            .collect(Collectors.toList());

        return new GiftDto(gift.getId(), gift.getCategory(), giftItemDtos.size(), giftItemDtos);
    }

    public GiftItemDto convertToGiftItemDto(GiftItem giftItem) {
        return new GiftItemDto(giftItem.getId(), giftItem.getGiftType(), giftItem.getContent(),
            giftItem.isUsed());
    }

    public GiftItemDetailDto convertToGiftItemDetailDto(GiftItem giftItem, String category, String mainTemplate,
        String sender) {
        return new GiftItemDetailDto(giftItem.getId(), giftItem.getGiftType(), giftItem.getContent(),
            giftItem.isUsed(), category, mainTemplate, sender);
    }

    public GiftItemSearchDto convertToGiftItemSearchDto(GiftItem giftItem) {
        Gift gift = giftItem.getGift();
        Event event = giftItem.getGift().getEvent();
        EventLog eventLog = giftItem.getEventLog();

        return new GiftItemSearchDto(giftItem.getId(), giftItem.getGiftType(), giftItem.getContent(), giftItem.isUsed(),
            gift.getCategory(), event.getMainTemplate(), event.getMember().getName()
            , eventLog.getCreatedAt());
    }
}
