package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;

public class TestGiftItemFactory {

    public static GiftItem createTestGiftItem(String content, Gift gift) {
        GiftItem giftItem = new GiftItem(GiftType.IMAGE, content, false);
        giftItem.changeGift(gift);
        return giftItem;
    }
}
