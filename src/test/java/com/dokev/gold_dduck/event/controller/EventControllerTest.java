package com.dokev.gold_dduck.event.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dokev.gold_dduck.common.error.ErrorCode;
import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
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
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EventSaveConverter eventSaveConverter;

    @Test
    @DisplayName("이벤트 생성 테스트 - 성공")
    void saveEventTest() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.createTestMember();
        entityManager.persist(testMember);

        Event newEvent = TestEventFactory.createEvent(testMember);

        EventSaveDto eventSaveDto = eventSaveConverter.convertToEventSaveDto(newEvent);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventSaveDto))
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
    @DisplayName("이벤트 생성 실패 (존재하지 않는 memberId)")
    void saveEventFailureTest() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.createTestMember();
        entityManager.persist(testMember);

        Event newEvent = TestEventFactory.createEvent(testMember);

        EventSaveDto eventSaveDto = eventSaveConverter.convertToEventSaveDto(newEvent);
        eventSaveDto.changedMemberId(eventSaveDto.getMemberId() + 1L);

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventSaveDto))
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
    @DisplayName("이벤트 생성 실패 (Invalid Input Value - 선물, 선물 아이템이 비어 있는 경우)")
    void saveEventFailureTest2() throws Exception {
        // GIVEN
        Member testMember = TestMemberFactory.createTestMember();
        entityManager.persist(testMember);

        Event newEvent = TestEventFactory.builder(testMember).build();

        EventSaveDto eventSaveDto = eventSaveConverter.convertToEventSaveDto(newEvent);
        eventSaveDto.changedMemberId(eventSaveDto.getMemberId());

        // WHEN
        ResultActions resultActions = mockMvc.perform(
            post("/api/v1/events")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventSaveDto))
        );

        // THEN
        resultActions.andDo(print())
            .andExpectAll(
                jsonPath("$.success", is(false)),
                jsonPath("$.data", is(nullValue())),
                jsonPath("$.error.code", is(ErrorCode.INVALID_INPUT_VALUE.getCode())),
                jsonPath("$.error.message", containsString(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
            );
    }
}
