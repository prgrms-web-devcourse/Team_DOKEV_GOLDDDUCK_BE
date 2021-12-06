package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.gift.domain.Gift;

public class TestGiftFactory {

    public static Gift createTestGift(String category, Integer itemCount, Event event) {
        Gift gift = new Gift("category " + category, itemCount);
        gift.changeEvent(event);
        return gift;
    }
}
