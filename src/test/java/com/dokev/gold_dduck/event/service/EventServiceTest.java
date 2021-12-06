package com.dokev.gold_dduck.event.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.dokev.gold_dduck.event.converter.EventSaveConverter;
import com.dokev.gold_dduck.event.domain.Event;
import com.dokev.gold_dduck.event.dto.EventSaveDto;
import com.dokev.gold_dduck.event.repository.EventRepository;
import com.dokev.gold_dduck.factory.TestEventFactory;
import com.dokev.gold_dduck.factory.TestMemberFactory;
import com.dokev.gold_dduck.member.domain.Member;
import com.dokev.gold_dduck.member.repository.MemberRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private EventSaveConverter eventConverter;

    @Mock
    private EventRepository eventRepository;

    @Test
    @DisplayName("이벤트 코드 생성 테스트 - 성공")
    void eventCreateTest() {
        // GIVEN
        Member testMember = TestMemberFactory.createTestMember();

        Event newEvent = TestEventFactory.createEvent(testMember);

        EventSaveDto testEventSaveRequest = mock(EventSaveDto.class);

        given(memberRepository.findById(any())).willReturn(Optional.of(testMember));
        given(eventConverter.convertToEvent(testEventSaveRequest, testMember)).willReturn(newEvent);
        given(eventRepository.save(newEvent)).willReturn(newEvent);

        // WHEN
        UUID eventCode = eventService.saveEvent(testEventSaveRequest);

        // THEN
        assertNotNull(eventCode);
        assertThat(eventCode, is(newEvent.getCode()));
    }
}
