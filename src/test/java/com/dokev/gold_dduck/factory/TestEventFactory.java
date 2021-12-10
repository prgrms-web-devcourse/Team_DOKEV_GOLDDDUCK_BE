package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.Event.EventBuilder;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TestEventFactory {

    public static EventBuilder builder(Member member) {
        return Event.builder(
            "title1",
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

    public static EventDto createEventDto(UUID eventCode, MemberDto memberDto, List<GiftDto> giftDtos) {
        return new EventDto(1L,
            "title1",
            GiftChoiceType.FIFO,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(10),
            eventCode,
            EventProgressStatus.RUNNING,
            "template1",
            60,
            memberDto,
            giftDtos);
    }
}
