package com.dokev.gold_dduck.event.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

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

        // WHEN, THEN
        mockMvc.perform(
                post("/api/v1/events")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventSaveDto))
            )
            .andDo(print())
            .andExpectAll(status().isOk(),
                jsonPath("$.success", is(true)),
                jsonPath("$.data", is(notNullValue()))
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

        // WHEN, THEN
        mockMvc.perform(
                post("/api/v1/events")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventSaveDto))
            )
            .andDo(print())
            .andExpectAll(
                jsonPath("$.success", is(false)),
                jsonPath("$.data", is(nullValue())),
                jsonPath("$.error.code", is("E001")),
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

        // WHEN, THEN
        mockMvc.perform(
                post("/api/v1/events")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(eventSaveDto))
            )
            .andDo(print())
            .andExpectAll(
                jsonPath("$.success", is(false)),
                jsonPath("$.data", is(nullValue())),
                jsonPath("$.error.code", is("C004")),
                jsonPath("$.error.message", containsString("Invalid Input Value"))
            );
    }
}
