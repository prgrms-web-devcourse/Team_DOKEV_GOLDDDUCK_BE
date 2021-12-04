package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.gift.domain.Gift;

public class TestGiftFactory {
    public static Gift createTestGift(String category, Integer itemCount) {
        return new Gift("category " + category, itemCount);
    }
}
