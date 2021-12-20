package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import java.util.ArrayList;
import java.util.List;

public class TestGiftFactory {

    public static Gift createTestGift(String category, Integer itemCount, Event event) {
        Gift gift = new Gift("category " + category, itemCount);
        gift.changeEvent(event);
        return gift;
    }

    public static List<GiftDto> createTestGiftDtos(List<GiftItemDto> giftItemDtos) {
        ArrayList<GiftDto> giftDtos = new ArrayList<>();
        giftDtos.add(new GiftDto(1L, "category gift0", 3, false, giftItemDtos));
        giftDtos.add(new GiftDto(2L, "category gift1", 3, false, giftItemDtos));
        giftDtos.add(new GiftDto(3L, "category gift2", 3, false, giftItemDtos));

        return giftDtos;
    }
}
