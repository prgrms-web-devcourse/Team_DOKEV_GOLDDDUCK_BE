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
        boolean soldout = giftItems.stream().allMatch(giftItem -> giftItem.getMember() != null);
        return new GiftDto(gift.getId(), gift.getCategory(), giftItemDtos.size(), soldout, giftItemDtos);
    }

    public GiftItemDto convertToGiftItemDto(GiftItem giftItem) {
        return new GiftItemDto(giftItem.getId(), giftItem.getGiftType(), giftItem.getContent(),
            giftItem.isUsed());
    }

    public GiftItemDetailDto convertToGiftItemDetailDto(GiftItem giftItem, Long giftId, String category, String mainTemplate,
        String sender) {
        return new GiftItemDetailDto(giftItem.getId(), giftItem.getGiftType(), giftItem.getContent(),
            giftItem.isUsed(), giftId, category, mainTemplate, sender);
    }
}
