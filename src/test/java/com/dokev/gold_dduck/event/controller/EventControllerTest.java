package com.dokev.gold_dduck.event.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dokev.gold_dduck.common.error.ErrorCode;
import com.dokev.gold_dduck.event.converter.EventFindConverter;
import com.dokev.gold_dduck.event.converter.EventLogConverter;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.domain.EventLog;
import com.dokev.gold_dduck.event.domain.EventProgressStatus;
import com.dokev.gold_dduck.event.dto.EventDto;
import com.dokev.gold_dduck.event.dto.EventLogDto;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.dto.GiftItemSaveDto;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.gift.domain.Gift;
import com.dokev.gold_dduck.gift.domain.GiftItem;
import com.dokev.gold_dduck.gift.domain.GiftType;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.security.WithMockJwtAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@WithMockJwtAuthentication
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventSaveConverter eventSaveConverter;

    @Autowired
    private EventFindConverter eventFindConverter;

    @Autowired
    private EventLogConverter eventLogConverter;

    @Test
    @DisplayName("이벤트 생성 테스트 - 성공")
    void saveEventTest() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.getUserMember(entityManager);

        EventSaveDto eventSaveDto = TestEventFactory.createEventSaveDto(testMember);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            multipart("/api/v1/events")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .flashAttr("eventSaveDto", eventSaveDto)
        );

        // THEN
        resultActions.andDo(print())
            .andExpectAll(status().isOk(),
                jsonPath("$.success", is(true)),
                jsonPath("$.data", is(notNullValue())),
                jsonPath("$.data", matchesPattern("([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})"))
            );
    }

    @Test
    @DisplayName("이벤트 조회 테스트 - 성공")
    void findEventByCode() throws Exception {
        //given
        Member member = TestMemberFactory.getUserMember(entityManager);

        Event event = TestEventFactory.createEvent(member);
        event.changeMember(member);
        entityManager.persist(event);

        entityManager.clear();

        EventDto eventDto = eventFindConverter.convertToEventDto(event);

        //when, then
        mockMvc.perform(get("/api/v1/events/{event-code}", event.getCode()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data.eventId", is(eventDto.getEventId().intValue())))
            .andExpect(jsonPath("$.data.title", is(eventDto.getTitle())))
            .andExpect(jsonPath("$.data.giftChoiceType", is(eventDto.getGiftChoiceType().toString())))
            .andExpect(jsonPath("$.data.code", is(eventDto.getCode().toString())))
            .andExpect(jsonPath("$.data.eventProgressStatus", is(eventDto.getEventProgressStatus().toString())))
            .andExpect(jsonPath("$.data.mainTemplate", is(eventDto.getMainTemplate())))
            .andExpect(jsonPath("$.data.maxParticipantCount", is(eventDto.getMaxParticipantCount())))
            .andExpect(jsonPath("$.data.member.id", is(eventDto.getMember().getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[0].id", is(eventDto.getGifts().get(0).getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[1].id", is(eventDto.getGifts().get(1).getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[2].id", is(eventDto.getGifts().get(2).getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[0].giftItems[0].id",
                is(eventDto.getGifts().get(0).getGiftItems().get(0).getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[0].giftItems[1].id",
                is(eventDto.getGifts().get(0).getGiftItems().get(1).getId().intValue())))
            .andExpect(jsonPath("$.data.gifts[0].giftItems[2].id",
                is(eventDto.getGifts().get(0).getGiftItems().get(2).getId().intValue())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("이벤트 생성 실패 (존재하지 않는 memberId)")
    void saveEventFailureTest() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.getUserMember(entityManager);

        Event newEvent = TestEventFactory.createEvent(testMember);

        EventSaveDto eventSaveDto = eventSaveConverter.convertToEventSaveDto(newEvent);
        eventSaveDto.changedMemberId(-1L);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/events")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .flashAttr("eventSaveDto", eventSaveDto)
        );

        // THEN
        resultActions.andDo(print())
            .andExpectAll(
                jsonPath("$.success", is(false)),
                jsonPath("$.data", is(nullValue())),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Member.class.getName()))
            );
    }

    @Test
    @DisplayName("이벤트 생성 실패 (선물이 비어 있는 경우)")
    void saveEventFailureTest2() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.getUserMember(entityManager);

        EventSaveDto eventSaveDto = TestEventFactory.createEventSaveDto(testMember);
        MultipartFile multipartFile = null;
        GiftItemSaveDto giftItemSaveDto = new GiftItemSaveDto(GiftType.IMAGE, multipartFile);
        eventSaveDto.getGifts().get(0).getGiftItems().add(giftItemSaveDto);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/events")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .flashAttr("eventSaveDto", eventSaveDto)
        );

        // THEN
        resultActions.andDo(print())
            .andExpectAll(
                jsonPath("$.success", is(false)),
                jsonPath("$.data", is(nullValue())),
                jsonPath("$.error.code", is(ErrorCode.GIFT_NOT_EMPTY.getCode())),
                jsonPath("$.error.message", containsString(ErrorCode.GIFT_NOT_EMPTY.getMessage()))
            );
    }

    @Test
    @DisplayName("이벤트 조회 테스트 - 실패")
    void findEventByCodeFailTest() throws Exception {
        mockMvc.perform(get("/api/v1/events/{event-code}", UUID.randomUUID()))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.data", is(nullValue())))
            .andExpect(jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())))
            .andExpect(jsonPath("$.error.message",
                containsString("해당 엔티티를 찾을 수 없습니다.")));

    }

    @Test
    @DisplayName("Member가 생성한 Event 최신순으로 페이징 조회 성공 테스트 - (page = null, size = null, 이벤트상태 = null)")
    void searchSimpleDescByMemberSuccessTest() throws Exception {
        //given
        Member userMember = TestMemberFactory.getUserMember(entityManager);
        Event closedEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.CLOSED)
            .build();
        Event runningEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event readyEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.READY)
            .build();
        entityManager.persist(closedEvent);
        entityManager.persist(runningEvent);
        entityManager.persist(readyEvent);
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/events", userMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(EventController.class),
                handler().methodName("searchSimpleDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.simpleEventList[*].eventId",
                    contains(readyEvent.getId().intValue(),
                        runningEvent.getId().intValue(),
                        closedEvent.getId().intValue())),
                jsonPath("$.data.pagination.totalPages", is(1)),
                jsonPath("$.data.pagination.totalElements", is(3)),
                jsonPath("$.data.pagination.currentPage", is(0)),
                jsonPath("$.data.pagination.offset", is(0)),
                jsonPath("$.data.pagination.size", is(4))
            );
    }

    @Test
    @DisplayName("Member가 생성한 Event 최신순으로 페이징 조회 성공 테스트 - (page = null, size = null, 이벤트상태 = 완료)")
    void searchSimpleDescByMemberSuccessTest2() throws Exception {
        //given
        Member userMember = TestMemberFactory.getUserMember(entityManager);
        Event closedEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.CLOSED)
            .build();
        Event runningEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event readyEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.READY)
            .build();
        entityManager.persist(closedEvent);
        entityManager.persist(runningEvent);
        entityManager.persist(readyEvent);
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/events", userMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("eventProgressStatus", EventProgressStatus.CLOSED.name())
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(EventController.class),
                handler().methodName("searchSimpleDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data..eventId", contains(closedEvent.getId().intValue())),
                jsonPath("$.data.pagination.totalPages", is(1)),
                jsonPath("$.data.pagination.totalElements", is(1)),
                jsonPath("$.data.pagination.currentPage", is(0)),
                jsonPath("$.data.pagination.offset", is(0)),
                jsonPath("$.data.pagination.size", is(4))
            );
    }

    @Test
    @DisplayName("Member가 생성한 Event 최신순으로 페이징 조회 성공 테스트 - (page = 1, size = 2, 이벤트상태 = 진행)")
    void searchSimpleDescByMemberSuccessTest3() throws Exception {
        //given
        Member userMember = TestMemberFactory.getUserMember(entityManager);
        Event closedEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.CLOSED)
            .build();
        Event runningEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event runningEvent2 = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event runningEvent3 = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event runningEvent4 = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event runningEvent5 = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.RUNNING)
            .build();
        Event readyEvent = TestEventFactory.builder(userMember)
            .eventProgressStatus(EventProgressStatus.READY)
            .build();
        entityManager.persist(closedEvent);
        entityManager.persist(runningEvent);
        entityManager.persist(runningEvent2);
        entityManager.persist(runningEvent3);
        entityManager.persist(runningEvent4);
        entityManager.persist(runningEvent5);
        entityManager.persist(readyEvent);
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/events", userMember.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("eventProgressStatus", EventProgressStatus.RUNNING.name())
                .param("page", "1")
                .param("size", "2")
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().isOk(),
                handler().handlerType(EventController.class),
                handler().methodName("searchSimpleDescByMember"),
                jsonPath("$.success", is(true)),
                jsonPath("$.error", is(nullValue())),
                jsonPath("$.data.simpleEventList.length()", is(2)),
                jsonPath("$.data.simpleEventList[*].eventId",
                    contains(runningEvent3.getId().intValue(), runningEvent2.getId().intValue())),
                jsonPath("$.data.pagination.totalPages", is(3)),
                jsonPath("$.data.pagination.totalElements", is(5)),
                jsonPath("$.data.pagination.currentPage", is(1)),
                jsonPath("$.data.pagination.offset", is(2)),
                jsonPath("$.data.pagination.size", is(2))
            );
    }

    @Test
    @DisplayName("Member가 생성한 Event 최신순으로 페이징 조회 실패 테스트 - (잘못된 memberId)")
    void searchSimpleDescByMemberFailureTest() throws Exception {
        //given
        Long invalidMemberId = -1L;
        //when
        ResultActions result = mockMvc.perform(
            get("/api/v1/members/{memberId}/events", invalidMemberId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andDo(print())
            .andExpectAll(
                status().is4xxClientError(),
                handler().handlerType(EventController.class),
                handler().methodName("searchSimpleDescByMember"),
                jsonPath("$.success", is(false)),
                jsonPath("$.error.code", is(ErrorCode.ENTITY_NOT_FOUND.getCode())),
                jsonPath("$.error.message", containsString(Member.class.getName()))
            );
    }

    @Test
    @DisplayName("Member가 생성한 이벤트의 당첨자 조회 테스트 - 성공")
    void searchWinners() throws Exception {
        Member member1 = TestMemberFactory.createTestMember(entityManager);
        Member member2 = TestMemberFactory.createTestMember(entityManager);
        Member member3 = TestMemberFactory.createTestMember(entityManager);
        Member member4 = TestMemberFactory.createTestMember(entityManager);
        Member member5 = TestMemberFactory.createTestMember(entityManager);
        Member member6 = TestMemberFactory.createTestMember(entityManager);
        Member member7 = TestMemberFactory.createTestMember(entityManager);
        Member member8 = TestMemberFactory.createTestMember(entityManager);

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        entityManager.persist(member5);
        entityManager.persist(member6);
        entityManager.persist(member7);
        entityManager.persist(member8);

        Event event = TestEventFactory.createEvent(member1);

        entityManager.persist(event);

        Gift gift1 = event.getGifts().get(0);
        Gift gift2 = event.getGifts().get(1);
        Gift gift3 = event.getGifts().get(2);

        GiftItem giftItem1 = gift1.getGiftItems().get(0);
        GiftItem giftItem2 = gift1.getGiftItems().get(1);
        GiftItem giftItem3 = gift2.getGiftItems().get(0);
        GiftItem giftItem4 = gift2.getGiftItems().get(1);
        GiftItem giftItem5 = gift3.getGiftItems().get(0);

        EventLog eventLog2 = new EventLog(event, member2, gift1, giftItem1);
        EventLog eventLog3 = new EventLog(event, member3, gift1, giftItem2);

        EventLog eventLog4 = new EventLog(event, member4, gift2, giftItem3);
        EventLog eventLog5 = new EventLog(event, member5, gift2, giftItem4);

        EventLog eventLog6 = new EventLog(event, member6, gift3, giftItem5);
        EventLog eventLog7 = new EventLog(event, member7, gift3, null);
        EventLog eventLog8 = new EventLog(event, member8, gift3, null);

        entityManager.persist(eventLog2);
        entityManager.persist(eventLog3);
        entityManager.persist(eventLog4);
        entityManager.persist(eventLog5);
        entityManager.persist(eventLog6);
        entityManager.persist(eventLog7);
        entityManager.persist(eventLog8);

        entityManager.clear();

        List<EventLog> eventLogs = Arrays.asList(eventLog2, eventLog3, eventLog4, eventLog5, eventLog6);
        List<EventLogDto> eventLogDtos = eventLogConverter.convertToEventLogDtos(eventLogs);

        mockMvc.perform(get("/api/v1/members/{memberId}/{eventId}/winners", member1.getId(), event.getId()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data[0].category", is(eventLogDtos.get(0).getCategory())))
            .andExpect(jsonPath("$.data[1].category", is(eventLogDtos.get(1).getCategory())))
            .andExpect(jsonPath("$.data[2].category", is(eventLogDtos.get(2).getCategory())))
            .andExpect(jsonPath("$.data[0].winners.size()", is(2)))
            .andExpect(jsonPath("$.data[1].winners.size()", is(2)))
            .andExpect(jsonPath("$.data[2].winners.size()", is(1)))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }

    @Test
    @DisplayName("이벤트 삭제 테스트 - 성공")
    void deleteEvent() throws Exception {
        Member member = TestMemberFactory.createTestMember(entityManager);
        entityManager.persist(member);
        Event event = TestEventFactory.createEvent(member);
        entityManager.persist(event);

        mockMvc.perform(delete("/api/v1/members/{memberId}/events/{eventId}", member.getId(), event.getId()))
            .andDo(print())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.data", is(event.getId().intValue())))
            .andExpect(jsonPath("$.error", is(nullValue())));
    }
}
