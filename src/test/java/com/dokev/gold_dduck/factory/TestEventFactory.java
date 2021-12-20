package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.Event.EventBuilder;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.gift.dto.GiftDto;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.dto.MemberDto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.mock.web.MockMultipartFile;

public class TestEventFactory {

    public static EventBuilder builder(Member member) {
        return Event.builder(
            "title1",
            GiftChoiceType.FIFO,
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusMinutes(10),
            EventProgressStatus.READY,
            "template1",
            60,
            9,
            51,
            member);
    }

    public static Event createEvent(Member member) {
        Event newEvent = TestEventFactory.builder(member).build();

        createGift(newEvent);

        return newEvent;
    }

    public static Event createRandomEvent(Member member) {
        Event newEvent = TestEventFactory.builder(member).giftChoiceType(GiftChoiceType.RANDOM).build();

        createGift(newEvent);

        return newEvent;
    }

    private static void createGift(Event newEvent) {
        for (int i = 0; i < 3; i++) {
            Gift testGift = TestGiftFactory.createTestGift("gift" + i, 3, newEvent);
            for (int j = 0; j < 3; j++) {
                TestGiftItemFactory.createTestGiftItem("image" + i, testGift);
            }
        }
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

    public static EventSaveDto createEventSaveDto(Member testMember) throws IOException {
        List<GiftSaveDto> giftSaveDtos = new ArrayList<>();

        List<GiftItemSaveDto> giftItemSaveDtos = new ArrayList<>();

        File imageFile = new File(System.getProperty("user.dir") + "/test-photo/test.png");

        MockMultipartFile image = new MockMultipartFile("images", "test.png", null,
            Files.readAllBytes(imageFile.toPath()));

        GiftItemSaveDto giftItemSaveDto1 = new GiftItemSaveDto(GiftType.IMAGE, image);
        GiftItemSaveDto giftItemSaveDto2 = new GiftItemSaveDto(GiftType.TEXT, "text1");
        GiftItemSaveDto giftItemSaveDto3 = new GiftItemSaveDto(GiftType.TEXT, "text2");

        giftItemSaveDtos.add(giftItemSaveDto1);
        giftItemSaveDtos.add(giftItemSaveDto2);
        giftItemSaveDtos.add(giftItemSaveDto3);

        GiftSaveDto giftSaveDto = new GiftSaveDto("gift1", giftItemSaveDtos);

        giftSaveDtos.add(giftSaveDto);

        return new EventSaveDto(testMember.getId(), "eventTitle1", giftSaveDtos, GiftChoiceType.FIFO, "template1",
            LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(5), 60);

    }
}
