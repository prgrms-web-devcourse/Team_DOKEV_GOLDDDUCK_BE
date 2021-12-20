package com.dokev.gold_dduck.gift.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dokev.gold_dduck.common.error.ErrorCode;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestGiftFactory;
import com.dokev.gold_dduck.factory.TestGiftItemFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.dto.GiftFifoChoiceDto;
import com.dokev.gold_dduck.gift.dto.GiftItemUpdateDto;
import com.dokev.gold_dduck.gift.dto.GiftRandomChoiceDto;
import com.dokev.gold_dduck.gift.service.GiftService;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.security.WithMockJwtAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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

@WithMockJwtAuthentication
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
        Long invalidEventId = -1L;
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
    @DisplayName("선착순으로 선물 받기 실패 테스트 (선물 재고 소진에 의한 이벤트 종료)")
    void chooseGiftItemByFIFOFailureTest5() throws Exception {
        //given
        Event givenEvent = givenEvent();
        Gift gift = givenGift(givenEvent);
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
                jsonPath("$.error.code", is(ErrorCode.GIFT_STOCK_OUT.getCode()))
            );
    }

    @Test
    @DisplayName("랜덤으로 선물 받기 실패 테스트 - (이벤트 종료 시간 넘어서 이벤트 종료됨)")
    void chooseGiftItemByRandomSuccessTest() throws Exception {
        //given
        Member givenMember = givenMember();
        Event endEvent = TestEventFactory.builder(givenMember).endAt(LocalDateTime.now().minusMinutes(1)).build();
        entityManager.persist(endEvent);
        GiftRandomChoiceDto giftRandomChoiceDto = new GiftRandomChoiceDto(endEvent.getId(),
            givenMember.getId());
        //when
        ResultActions result = mockMvc.perform(
            post("/api/v1/gifts/random")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftRandomChoiceDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().is4xxClientError(),
                handler().handlerType(GiftController.class),
                handler().methodName("chooseGiftItemByRandom"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.EVENT_CLOSED.getCode()))
            );
        Event findEvent = entityManager.find(Event.class, endEvent.getId());
        assertThat(findEvent.getEventProgressStatus(), is(EventProgressStatus.CLOSED));
    }

    @Test
    @DisplayName("Member가 받은 GiftItem 수령일 최신순으로 페이징 조회 성공 테스트 - (page = null, size = null, 사용여부 = null)")
    void searchDescByMemberSuccessTest() throws Exception {
        //given
        Event givenEvent1 = givenCompleteEvent();
        Event givenEvent2 = givenCompleteEvent();
        Member givenMember = givenEvent1.getMember();
        Gift gift1 = givenEvent1.getGifts().get(0);
        Gift gift2 = givenEvent2.getGifts().get(0);
        GiftItem targetGiftItem1 = gift1.getGiftItems().get(0);
        GiftItem targetGiftItem2 = gift2.getGiftItems().get(0);
        targetGiftItem1.changeUsed(true);
        entityManager.persist(new EventLog(givenEvent1, givenMember, gift1, targetGiftItem1));
        entityManager.persist(new EventLog(givenEvent2, givenMember, gift2, targetGiftItem2));
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/gifts", givenMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(GiftController.class),
                handler().methodName("searchDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.giftItemList.length()", is(2)),
                jsonPath("$.data.giftItemList[*].id",
                    contains(targetGiftItem2.getId().intValue(), targetGiftItem1.getId().intValue())),
                jsonPath("$.data.pagination.totalPages", is(1)),
                jsonPath("$.data.pagination.totalElements", is(2)),
                jsonPath("$.data.pagination.currentPage", is(0)),
                jsonPath("$.data.pagination.offset", is(0)),
                jsonPath("$.data.pagination.size", is(4))
            );
    }

    @Test
    @DisplayName("Member가 받은 GiftItem 수령일 최신순으로 페이징 조회 성공 테스트 - (page = 1, size = 2, 사용여부 = 사용)")
    void searchDescByMemberSuccessTest2() throws Exception {
        //given
        Event givenEvent1 = givenCompleteEvent();
        Event givenEvent2 = givenCompleteEvent();
        Member givenMember = givenEvent1.getMember();
        Gift gift1 = givenEvent1.getGifts().get(0);
        Gift gift2 = givenEvent2.getGifts().get(0);
        GiftItem targetGiftItem1 = gift1.getGiftItems().get(0);
        GiftItem targetGiftItem2 = gift1.getGiftItems().get(1);
        GiftItem targetGiftItem3 = gift2.getGiftItems().get(0);
        GiftItem targetGiftItem4 = gift2.getGiftItems().get(1);
        GiftItem targetGiftItem5 = gift2.getGiftItems().get(2);
        targetGiftItem1.changeUsed(true);
        targetGiftItem2.changeUsed(true);
        targetGiftItem3.changeUsed(true);
        targetGiftItem4.changeUsed(true);
        entityManager.persist(new EventLog(givenEvent1, givenMember, gift1, targetGiftItem1));
        entityManager.persist(new EventLog(givenEvent1, givenMember, gift1, targetGiftItem2));
        entityManager.persist(new EventLog(givenEvent2, givenMember, gift2, targetGiftItem3));
        entityManager.persist(new EventLog(givenEvent2, givenMember, gift2, targetGiftItem4));
        entityManager.persist(new EventLog(givenEvent2, givenMember, gift2, targetGiftItem5));
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/gifts", givenMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "1")
                .param("size", "2")
                .param("used", "true")
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(GiftController.class),
                handler().methodName("searchDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.giftItemList.length()", is(2)),
                jsonPath("$.data.giftItemList[*].id",
                    contains(targetGiftItem2.getId().intValue(), targetGiftItem1.getId().intValue())),
                jsonPath("$.data.pagination.totalPages", is(2)),
                jsonPath("$.data.pagination.totalElements", is(4)),
                jsonPath("$.data.pagination.currentPage", is(1)),
                jsonPath("$.data.pagination.offset", is(2)),
                jsonPath("$.data.pagination.size", is(2))
            );
    }

    @Test
    @DisplayName("Member가 받은 GiftItem 수령일 최신순으로 페이징 조회 성공 테스트 - (dto 값 검증)")
    void searchDescByMemberSuccessTest3() throws Exception {
        //given
        Event givenEvent1 = givenCompleteEvent();
        Member givenMember = givenEvent1.getMember();
        Gift gift1 = givenEvent1.getGifts().get(0);
        GiftItem targetGiftItem1 = gift1.getGiftItems().get(0);
        targetGiftItem1.changeUsed(true);
        entityManager.persist(new EventLog(givenEvent1, givenMember, gift1, targetGiftItem1));
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/gifts", givenMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(GiftController.class),
                handler().methodName("searchDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.giftItemList.length()", is(1)),
                jsonPath("$.data.giftItemList[0].id", is(targetGiftItem1.getId().intValue())),
                jsonPath("$.data.giftItemList[0].category", is(gift1.getCategory())),
                jsonPath("$.data.giftItemList[0].mainTemplate", is(givenEvent1.getMainTemplate()))
            );
    }

    @Test
    @DisplayName("Member가 생성한 Event 최신순으로 페이징 조회 실패 테스트 - (잘못된 memberId)")
    void searchDescByMemberFailureTest() throws Exception {
        //given
        Long invalidMemberId = -1L;
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/gifts", invalidMemberId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().is4xxClientError(),
                handler().handlerType(GiftController.class),
                handler().methodName("searchDescByMember"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Member.class.getName()))
            );
    }

    @Test
    @DisplayName("GiftItem 수정 성공 테스트")
    void updateGiftItemSuccessTest() throws Exception {
        //given
        Event givenEvent = givenCompleteEvent();
        Member givenMember = givenEvent.getMember();
        Gift givenGift = givenEvent.getGifts().get(0);
        GiftItem targetGiftItem = givenGift.getGiftItems().get(0);
        targetGiftItem.allocateMember(givenMember);
        GiftItemUpdateDto giftItemUpdateDto = new GiftItemUpdateDto(true);
        //when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/members/{memberId}/giftItems/{giftItemId}", givenMember.getId(), targetGiftItem.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftItemUpdateDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(GiftController.class),
                handler().methodName("updateGiftItem"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data", is(nullValue()))
            );
        GiftItem findGiftItem = entityManager.find(GiftItem.class, targetGiftItem.getId());
        assertThat(findGiftItem.isUsed(), is(true));
    }

    @Test
    @DisplayName("GiftItem 수정 실패 테스트 - (멤버가 할당되지 않은 선물)")
    void updateGiftItemFailureTest() throws Exception {
        //given
        Event givenEvent = givenCompleteEvent();
        Member givenMember = givenEvent.getMember();
        Gift givenGift = givenEvent.getGifts().get(0);
        GiftItem targetGiftItem = givenGift.getGiftItems().get(0);
        GiftItemUpdateDto giftItemUpdateDto = new GiftItemUpdateDto(true);
        //when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/members/{memberId}/giftItems/{giftItemId}", givenMember.getId(), targetGiftItem.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftItemUpdateDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().is4xxClientError(),
                handler().handlerType(GiftController.class),
                handler().methodName("updateGiftItem"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.MEMBER_GIFT_NOT_MATCHED.getCode()))
            );
    }

    @Test
    @DisplayName("GiftItem 수정 실패 테스트 - (요청 멤버와 선물에 할당된 멤버가 다른 경우)")
    void updateGiftItemFailureTest2() throws Exception {
        //given
        Event givenEvent = givenCompleteEvent();
        Member givenMember = givenEvent.getMember();
        Gift givenGift = givenEvent.getGifts().get(0);
        GiftItem targetGiftItem = givenGift.getGiftItems().get(0);
        targetGiftItem.allocateMember(TestMemberFactory.getAdminMember(entityManager));
        GiftItemUpdateDto giftItemUpdateDto = new GiftItemUpdateDto(false);
        //when
        ResultActions result = mockMvc.perform(
            patch("/api/v1/members/{memberId}/giftItems/{giftItemId}", givenMember.getId(), targetGiftItem.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(giftItemUpdateDto))
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().is4xxClientError(),
                handler().handlerType(GiftController.class),
                handler().methodName("updateGiftItem"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.MEMBER_GIFT_NOT_MATCHED.getCode()))
            );
    }

    @Test
    @DisplayName("선물 아이템 단일 조회 테스트 - 성공")
    void searchGiftItemById() throws Exception {
        Member admin = givenMember();
        Member user = givenMember();

        Event event = TestEventFactory.createEvent(admin);
        entityManager.persist(event);

        Gift gift = givenGift(event);
        GiftItem giftItem = givenGiftItem(gift, user);

        EventLog eventLog = new EventLog(event, user, gift, giftItem);
        entityManager.persist(eventLog);

        entityManager.clear();

        mockMvc.perform(get("/api/v1/members/{memberId}/giftItems/{giftItemId}"
                , user.getId(), giftItem.getId()))
            .andDo(print())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.id", is(giftItem.getId().intValue())))
            .andExpect(jsonPath("$.data.content", is(giftItem.getContent())))
            .andExpect(jsonPath("$.data.category", is(gift.getCategory())))
            .andExpect(jsonPath("$.data.mainTemplate", is(event.getMainTemplate())))
            .andExpect(jsonPath("$.data.sender", is(event.getMember().getName())))
            .andExpect(jsonPath("$.error", is(nullValue())));
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
        return TestMemberFactory.getUserMember(entityManager);
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
