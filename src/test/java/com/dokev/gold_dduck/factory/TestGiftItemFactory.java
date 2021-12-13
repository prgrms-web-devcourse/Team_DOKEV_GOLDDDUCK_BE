package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftItemDto;
import java.util.ArrayList;
import java.util.List;

public class TestGiftItemFactory {

    public static GiftItem createTestGiftItem(String content, Gift gift) {
        GiftItem giftItem = new GiftItem(GiftType.TEXT, content, false);
        giftItem.changeGift(gift);
        return giftItem;
    }

    public static List<GiftItemDto> createTestGiftItemDtos() {
        ArrayList<GiftItemDto> giftItemDtos = new ArrayList<>();
        giftItemDtos.add(new GiftItemDto(1L, GiftType.IMAGE, "image0", false));
        giftItemDtos.add(new GiftItemDto(2L, GiftType.IMAGE, "image1", false));
        giftItemDtos.add(new GiftItemDto(3L, GiftType.IMAGE, "image2", false));

        return giftItemDtos;
    }
}
