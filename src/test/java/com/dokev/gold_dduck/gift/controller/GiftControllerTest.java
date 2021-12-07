package com.dokev.gold_dduck.gift.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dokev.gold_dduck.common.error.ErrorCode;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestGiftFactory;
import com.dokev.gold_dduck.factory.TestGiftItemFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftFifoChoiceDto;
import com.dokev.gold_dduck.gift.service.GiftService;
import com.dokev.gold_dduck.member.domain.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class GiftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GiftService giftService;

    @Test
    @DisplayName("선착순으로 선물 받기 성공 테스트")
    void chooseGiftItemByFIFOSuccessTest() throws Exception {
        //given
        Event givenEvent = givenCompleteEvent();
        Gift chosenGift = givenEvent.getGifts().get(0);
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(givenEvent.getId(), givenEvent.getMember().getId(),
            chosenGift.getId());
        GiftItem firstGiftItem = chosenGift.getGiftItems().get(0);
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.id", is(firstGiftItem.getId().intValue())),
                jsonPath("$.data.giftType", is(firstGiftItem.getGiftType().toString())),
                jsonPath("$.data.content", is(firstGiftItem.getContent()))
            );
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 eventId)")
    void chooseGiftItemByFIFOFailureTest() throws Exception {
        //given
        Long invalidEventId = 1L;
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(invalidEventId, 1L, 1L);
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Event.class.getName()))
            );
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 memberId)")
    void chooseGiftItemByFIFOFailureTest2() throws Exception {
        //given
        Event givenEvent = givenEvent();
        Long invalidMemberId = -1L;
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(givenEvent.getId(), invalidMemberId, 1L);
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Member.class.getName()))
            );
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (이미 이벤트에 참여한 멤버)")
    void chooseGiftItemByFIFOFailureTest3() throws Exception {
        //given
        Event givenEvent = givenCompleteEvent();
        Gift chosenGift = givenEvent.getGifts().get(0);
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(givenEvent.getId(), givenEvent.getMember().getId(),
            chosenGift.getId());
        giftService.chooseGiftItemByFIFO(givenEvent.getId(), givenEvent.getMember().getId(), chosenGift.getId());
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.EVENT_ALREADY_PARTICIPATED.getCode())),
                jsonPath("$.error.message", containsString(ErrorCode.EVENT_ALREADY_PARTICIPATED.getMessage()))
            );
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (잘못된 giftId)")
    void chooseGiftItemByFIFOFailureTest4() throws Exception {
        //given
        Event givenEvent = givenEvent();
        Long invalidGiftId = -1L;
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(givenEvent.getId(), givenEvent.getMember().getId(),
            invalidGiftId);
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Gift.class.getName()))
            );
    }

    @Test
    @DisplayName("선착순으로 선물 받기 실패 테스트 (선물 재고 부족)")
    void chooseGiftItemByFIFOFailureTest5() throws Exception {
        //given
        Event givenEvent = givenEvent();
        Gift gift = givenGift(givenEvent);
        GiftItem giftItem = givenGiftItem(gift, givenEvent.getMember());
        GiftFifoChoiceDto giftFifoChoiceDto = new GiftFifoChoiceDto(givenEvent.getId(), givenEvent.getMember().getId(),
            gift.getId());
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/fifo")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftFifoChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByFIFO"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.GIFT_STOCK_OUT.getCode())),
                jsonPath("$.error.message", containsString(ErrorCode.GIFT_STOCK_OUT.getMessage()))
            );
    }

    private Event givenCompleteEvent() {
        Member member = givenMember();
        Event event = TestEventFactory.createEvent(member);
        entityManager.persist(event);
        return event;
    }

    private Event givenEvent() {
        Member member = givenMember();
        Event event = TestEventFactory.builder(member).build();
        entityManager.persist(event);
        return event;
    }

    private Member givenMember() {
        Member member = TestMemberFactory.createTestMember();
        entityManager.persist(member);
        return member;
    }

    private Gift givenGift(Event event) {
        Gift gift = TestGiftFactory.createTestGift("커피", 0, event);
        entityManager.persist(gift);
        return gift;
    }

    private GiftItem givenGiftItem(Gift gift, Member member) {
        GiftItem giftItem = TestGiftItemFactory.createTestGiftItem("스타벅스 커피1", gift);
        giftItem.allocateMember(member);
        entityManager.persist(giftItem);
        return giftItem;
    }
}
