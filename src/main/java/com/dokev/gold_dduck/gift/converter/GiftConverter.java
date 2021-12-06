package com.dokev.gold_dduck.gift.converter;

import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GiftConverter {

    private static final int EMPTY = 0;

    public GiftDto convertToGiftDto(Gift gift) {
        List<GiftItem> giftItems = gift.getGiftItems();
        List<GiftItemDto> giftItemDtos = giftItems.stream()
                .map(this::convertToGiftItemDto)
                .collect(Collectors.toList());

        if (gift.getItemCount().isEmpty()) {
            return new GiftDto(gift.getId(), gift.getCategory(), EMPTY, giftItemDtos);
        }

        return new GiftDto(gift.getId(), gift.getCategory(), gift.getItemCount().get(), giftItemDtos);
    }

    private GiftItemDto convertToGiftItemDto(GiftItem giftItem) {
        return new GiftItemDto(giftItem.getId(), giftItem.getGiftType().toString(), giftItem.getContent(),
                giftItem.isUsed());
    }

}
