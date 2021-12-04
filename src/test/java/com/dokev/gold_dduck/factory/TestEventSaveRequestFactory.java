package com.dokev.gold_dduck.factory;

import com.dokev.gold_dduck.event.domain.GiftChoiceType;
import com.dokev.gold_dduck.event.dto.EventSaveRequest;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.event.dto.GiftSaveDto;
import com.dokev.gold_dduck.gift.domain.GiftType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestEventSaveRequestFactory {

    public static EventSaveRequest createTestEventSaveRequest() {
        return new EventSaveRequest(1L,
            createTestGiftSaveDtos(),
            GiftChoiceType.FIFO,
            "template1",
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(5),
            100);
    }

    public static List<GiftSaveDto> createTestGiftSaveDtos() {
        List<GiftSaveDto> giftSaveDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GiftSaveDto giftSaveDto = new GiftSaveDto("category" + i, createTestGiftItemDtos());
            giftSaveDtos.add(giftSaveDto);
        }
        return giftSaveDtos;
    }

    public static List<GiftItemSaveDto> createTestGiftItemDtos() {
        List<GiftItemSaveDto> giftItemSaveDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GiftItemSaveDto giftItemSaveDto = new GiftItemSaveDto(GiftType.IMAGE, "image" + i);
            giftItemSaveDtos.add(giftItemSaveDto);
        }
        return giftItemSaveDtos;
    }
}
