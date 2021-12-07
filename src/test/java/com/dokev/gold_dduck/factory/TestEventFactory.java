package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.Event.EventBuilder;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.member.domain.Member;
import java.time.LocalDateTime;

public class TestEventFactory {

    public static EventBuilder builder(Member member) {
        return Event.builder(
            GiftChoiceType.FIFO,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(10),
            EventProgressStatus.RUNNING,
            "template1",
            60,
            member);
    }

    public static Event createEvent(Member member) {
        Event newEvent = TestEventFactory.builder(member).build();

        for (int i = 0; i < 3; i++) {
            Gift testGift = TestGiftFactory.createTestGift("gift" + i, 3, newEvent);
            for (int j = 0; j < 3; j++) {
                TestGiftItemFactory.createTestGiftItem("image" + i, testGift);
            }
        }

        return newEvent;
    }
}
