package com.dokev.gold_dduck.gift.converter;

import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftItemResponseDto;
import com.dokev.gold_dduck.gift.dto.GiftResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class GiftConverter {

    public GiftResponseDto convertToGiftResponseDto(Gift gift) {
        List<GiftItem> giftItems = gift.getGiftItems();
        List<GiftItemResponseDto> giftItemDtos = giftItems.stream()
                .map(this::convertToGiftItemResponseDto)
                .collect(Collectors.toList());

        if (gift.getItemCount().isEmpty()) {
            return new GiftResponseDto(gift.getId(), gift.getCategory(), 0, giftItemDtos);
        }

        return new GiftResponseDto(gift.getId(), gift.getCategory(), gift.getItemCount().get(), giftItemDtos);
    }

    public GiftItemResponseDto convertToGiftItemResponseDto(GiftItem giftItem) {
        return new GiftItemResponseDto(giftItem.getId(), giftItem.getGiftType().toString(), giftItem.getContent(),
                giftItem.isUsed());
    }

}
